package com.efolder.sbs.core.export;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.efolder.sbs.core.ErrorCode;
import com.efolder.sbs.core.SbsException;
import com.efolder.sbs.entities.BackupTask;
import com.efolder.sbs.entities.Todo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DefaultBackupDataProcessor implements BackupDataProcessor<InputStream> {
	private static final String ROW_DELIMITER = ";";
	@Value("${backups.dir}")
	private String backupDir;
	@Autowired
	private JsonFactory jsonFactory;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private BackupsStorage<InputStream, OutputStream> backupStorage;

	public void process(BackupTask task, InputStream todosStream) throws JsonParseException, IOException {
		JsonParser parser = jsonFactory.createParser(todosStream);
		parser.setCodec(objectMapper);

		String backupId = task.getId();

		try (OutputStream backupFile = backupStorage.outputStream(backupId)) {

			if (parser.isClosed()) {
				throw new SbsException(ErrorCode.BAD_TODOS_DATA_CLOSED, "backup stream is closed");
			}

			parser.nextToken();
			if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
				throw new SbsException(ErrorCode.BAD_TODOS_DATA_UNEXPECTED_TOKEN, "backup stream not start with START_ARRAY");
			}

			parser.nextToken();
			// user obj(s) started
			String userName = null;
			while (!parser.isClosed() && parser.getCurrentToken() != JsonToken.END_ARRAY) {
				parser.nextToken(); // current token is fieldName
				if (parser.getCurrentToken() == JsonToken.FIELD_NAME) {
					String fieldName = parser.getCurrentName();
					parser.nextToken();
					switch (fieldName) {
					case "id":
						break;
					case "username":
						userName = parser.getText();
						break;
					case "email":
						break;
					case "todos":
						processTodos(backupFile, userName, parser);

						break;
					default:
						throw new SbsException(ErrorCode.BAD_TODOS_DATA_UNEXPECTED_TOKEN, String.format("Unexpected field occured: {}", fieldName));
					}
				} else if (parser.getCurrentToken() == JsonToken.END_OBJECT) {
					// current user obj finished
					parser.nextToken();
					if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
						parser.nextToken();
					}
				}
			}

		}
	}

	private void processTodos(OutputStream backupFile, String userName, JsonParser parser) throws JsonParseException, IOException {
		if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
			throw new SbsException(ErrorCode.BAD_TODOS_DATA_UNEXPECTED_TOKEN, "Can't parse todos field");
		}
		parser.nextToken(); // start obj

		while (parser.getCurrentToken() != null && parser.getCurrentToken() != JsonToken.END_ARRAY) {
			if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
				Todo todo = new Todo();
				while (parser.getCurrentToken() != JsonToken.END_OBJECT) {
					parser.nextToken(); // field name
					if (parser.getCurrentToken() != JsonToken.END_OBJECT) {
						String fieldName = parser.getCurrentName();
						parser.nextToken();
						switch (fieldName) {
						case "id":
							todo.setId(parser.getText());
							break;
						case "subject":
							todo.setSubject(parser.getText());
							break;
						case "dueDate":
							todo.setDueDate(parser.getText());
							break;
						case "done":
							todo.setDone(parser.getText());
							break;
						default:
							throw new SbsException(ErrorCode.BAD_TODOS_DATA_UNEXPECTED_TOKEN, "An unknown todo's field occured");
						}
					}
				}
				String csvRecord = buildCsvRecord(userName, todo);
				backupFile.write(csvRecord.getBytes());
				parser.nextToken();
			}
		}
	}

	protected String buildCsvRecord(String userName, Todo todo) {
		return new StringBuilder()
				.append(userName).append(ROW_DELIMITER)
				.append(todo.getId()).append(ROW_DELIMITER)
				.append(todo.getSubject()).append(ROW_DELIMITER)
				.append(todo.getDueDate()).append(ROW_DELIMITER)
				.append(todo.getDone())
				.append('\n').toString();
	}
}
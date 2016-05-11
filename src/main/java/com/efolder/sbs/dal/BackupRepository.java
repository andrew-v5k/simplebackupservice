package com.efolder.sbs.dal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.efolder.sbs.entities.BackupTask;

@Repository
public interface BackupRepository extends CrudRepository<BackupTask, String> {
}
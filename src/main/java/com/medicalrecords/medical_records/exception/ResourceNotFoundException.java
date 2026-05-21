package com.medicalrecords.medical_records.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
//хвърля се когато не намира нещо в БД
//връща 404 като отговор
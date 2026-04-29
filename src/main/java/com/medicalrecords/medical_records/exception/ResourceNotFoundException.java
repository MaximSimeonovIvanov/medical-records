package com.medicalrecords.medical_records.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
// хвърля се когато не намираме нещо в БД
// ще върне 404 като отговор
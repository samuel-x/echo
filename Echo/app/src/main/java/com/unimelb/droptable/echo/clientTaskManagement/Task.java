package com.unimelb.droptable.echo.clientTaskManagement;


import org.immutables.serial.Serial;
import org.immutables.value.Value;

@Value.Immutable
@Serial.Structural
public interface Task {
    String getTitle();
    String getAddress();
    String getNotes();
    String getCategory();
    String getSubCategory();
}
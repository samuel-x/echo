package com.unimelb.droptable.echo.clientTaskManagement;

import android.support.annotation.Nullable;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

@Value.Immutable
@Serial.Structural
public interface Task {
    String getTitle();
    String getAddress();
    String getNotes();
    @Nullable
    String getCategory();
    @Nullable
    String getSubCategory();
}
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
    String getStatus();
    String getAp();
    @Nullable
    String getPaymentAmount();
    @Nullable
    String getId();
    @Nullable
    String getAssistant();
    @Nullable
    String getCategory();
    @Nullable
    String getSubCategory();
}
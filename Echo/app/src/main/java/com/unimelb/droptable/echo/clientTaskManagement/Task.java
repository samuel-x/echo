package com.unimelb.droptable.echo.clientTaskManagement;

import android.support.annotation.Nullable;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

/**
 * This where we define our Task object, or "ImmutableTask". We're using a library called
 * Immutables, which allows us to define an interface such as this, annotate it with
 * @Value.Immutable, and it will auto generate a fully functional object for us. In this case,
 * ImmutableTask.
 */
@Value.Immutable
@Serial.Structural
public interface Task {
    String getTitle();
    String getAddress();
    String getNotes();
    String getStatus();
    String getAp();
    String getLastPhase();
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
    @Nullable
    String getLatitude();
    @Nullable
    String getLongitude();
}
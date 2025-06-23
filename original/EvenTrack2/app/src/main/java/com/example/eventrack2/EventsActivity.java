package com.example.eventrack2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {

    EditText eventNameEditText, eventDateEditText, eventLocationEditText;
    Button submitEventButton, showEventsButton, deleteEventButton, updateEventButton, homeButton;
    ListView eventsListView;
    DatabaseHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> eventsList;
    int selectedEventId = -1;  // To track selected event for update or delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_main);

        // Initialize UI components
        eventNameEditText = findViewById(R.id.editTextEventName);
        eventDateEditText = findViewById(R.id.editTextEventDate);
        eventLocationEditText = findViewById(R.id.editTextEventLocation);
        submitEventButton = findViewById(R.id.buttonSaveEvent);
        showEventsButton = findViewById(R.id.buttonShowEvents);
        deleteEventButton = findViewById(R.id.buttonDeleteEvent);
        updateEventButton = findViewById(R.id.buttonUpdateEvent);
        eventsListView = findViewById(R.id.listViewEvents);
        homeButton = findViewById(R.id.buttonHome); // Home button initialization

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        eventsList = new ArrayList<>();

        // Set up ListView adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventsList);
        eventsListView.setAdapter(adapter);

        // Save event button click listener
        submitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameEditText.getText().toString().trim();
                String eventDate = eventDateEditText.getText().toString().trim();
                String eventLocation = eventLocationEditText.getText().toString().trim();

                if (eventName.isEmpty() || eventDate.isEmpty() || eventLocation.isEmpty()) {
                    Toast.makeText(EventsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = dbHelper.insertEvent(eventName, eventDate, eventLocation);
                    if (isInserted) {
                        Toast.makeText(EventsActivity.this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(EventsActivity.this, "Failed to save event", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Show events button click listener
        showEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEvents();
            }
        });

        // Delete event button click listener
        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEventId != -1) {
                    boolean isDeleted = dbHelper.deleteEvent(selectedEventId);
                    if (isDeleted) {
                        Toast.makeText(EventsActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                        loadEvents();
                        clearFields();
                    } else {
                        Toast.makeText(EventsActivity.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EventsActivity.this, "Please select an event to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update event button click listener
        updateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEventId != -1) {
                    String eventName = eventNameEditText.getText().toString().trim();
                    String eventDate = eventDateEditText.getText().toString().trim();
                    String eventLocation = eventLocationEditText.getText().toString().trim();

                    if (eventName.isEmpty() || eventDate.isEmpty() || eventLocation.isEmpty()) {
                        Toast.makeText(EventsActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean isUpdated = dbHelper.updateEvent(selectedEventId, eventName, eventDate, eventLocation);
                        if (isUpdated) {
                            Toast.makeText(EventsActivity.this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                            loadEvents();
                            clearFields();
                        } else {
                            Toast.makeText(EventsActivity.this, "Failed to update event", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(EventsActivity.this, "Please select an event to update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Home button click listener to navigate back
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(EventsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clears back stack
                startActivity(intent);
                finish();
            }
        });

        // Set up ListView item click listener to select event
        eventsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEvent = eventsList.get(position);
            String[] eventDetails = selectedEvent.split("\n");
            String eventName = eventDetails[0].replace("Event: ", "");
            String eventDate = eventDetails[1].replace("Date: ", "");
            String eventLocation = eventDetails[2].replace("Location: ", "");

            eventNameEditText.setText(eventName);
            eventDateEditText.setText(eventDate);
            eventLocationEditText.setText(eventLocation);

            // Get the event ID from the database
            Cursor cursor = dbHelper.getEventByName(eventName);
            if (cursor.moveToFirst()) {
                selectedEventId = cursor.getInt(cursor.getColumnIndex("id"));
            }
        });
    }

    // Load events from the database into the ListView
    private void loadEvents() {
        Cursor cursor = dbHelper.getAllEvents();
        eventsList.clear();

        if (cursor.getCount() == 0) {
            Toast.makeText(EventsActivity.this, "No events found", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            String event = "Event: " + cursor.getString(1) + "\n"
                    + "Date: " + cursor.getString(2) + "\n"
                    + "Location: " + cursor.getString(3);
            eventsList.add(event);
        }
        adapter.notifyDataSetChanged();
    }

    private void clearFields() {
        eventNameEditText.setText("");
        eventDateEditText.setText("");
        eventLocationEditText.setText("");
        selectedEventId = -1;
    }
}

package com.service;

import DAO.ConferenceDAO;
import com.entity.Conference;
import com.entity.Role;
import com.entity.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceService {
    private static final Logger LOGGER = Logger.getLogger(ConferenceService.class.getName());
    private final ConferenceDAO conferenceDAO;

    public ConferenceService() {
        this(new ConferenceDAO());
    }

    public ConferenceService(ConferenceDAO conferenceDAO) {
        this.conferenceDAO = Objects.requireNonNull(conferenceDAO);
    }

    public List<Conference> getAllConferences() throws SQLException {
        return conferenceDAO.getAllConferences();
    }

    public Conference getConference(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("A valid conference must be selected.");
        }

        return conferenceDAO.getConferenceById(id);
    }

    public int createConference(User actor, Conference conference) throws SQLException {
        requireAdministrator(actor);
        validateConference(conference);
        return conferenceDAO.addConference(conference);
    }

    public void updateConference(User actor, Conference conference) throws SQLException {
        requireAdministrator(actor);
        validateConference(conference);
        Conference existing = conferenceDAO.getConferenceById(conference.getConferenceId());

        if (existing == null) {
            throw new IllegalArgumentException("The selected conference no longer exists.");
        }

        int confirmed = conferenceDAO.countConfirmedRegistrations(conference.getConferenceId());

        if (conference.getCapacity() < confirmed) {
            throw new IllegalArgumentException("Capacity cannot be lower than the number of confirmed registrations (" + confirmed + ").");
        }
        if (!conferenceDAO.updateConference(conference)) {
            throw new IllegalArgumentException("The conference could not be updated.");
        }
    }

    public void deleteConference(User actor, int conferenceId) throws SQLException {
        requireAdministrator(actor);
        Conference existing = conferenceDAO.getConferenceById(conferenceId);

        if (existing == null) {
            throw new IllegalArgumentException("The selected conference no longer exists.");
        }
        if (conferenceDAO.hasRegistrations(conferenceId)) {
            throw new IllegalArgumentException("A conference cannot be deleted while registrations exist.");
        }
        if (!conferenceDAO.deleteConference(conferenceId)) {
            throw new IllegalArgumentException("The conference could not be deleted.");
        }
    }

    private static void requireAdministrator(User actor) {
        if (actor == null || actor.getRole() != Role.ADMIN) {
            throw new SecurityException("Administrator permission is required for this action.");
        }
    }

    private static void validateConference(Conference c) {
        if (c == null) {
            throw new IllegalArgumentException("Conference information is required.");
        }
        if (c.getTitle() == null || c.getTitle().isBlank()) {
            throw new IllegalArgumentException("Conference title is required.");
        }
        if (c.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        if (c.getStartTime() == null || c.getEndTime() == null || !c.getEndTime().isAfter(c.getStartTime())) {
            throw new IllegalArgumentException("The end date and time must be after the start date and time.");
        }
    }
}

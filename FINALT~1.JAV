package org.camunda.bpm.myproject.electronic_equipment_competitions;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;

public class FinalTaskAssignmentListener implements TaskListener {

    // TODO: Set Mail Server Properties
    private static final String HOST = "smtp.isc.tuc.gr";
    private static final int PORT = 587;
    private static final String USER = "promsystem@isc.tuc.gr";
    private static final String PWD = "pilotAksio!0801";

    private final static Logger LOGGER = Logger.getLogger(FinalTaskAssignmentListener.class.getName());

    public void notify(DelegateTask delegateTask) {

        String assignee = delegateTask.getAssignee();
        String taskId = delegateTask.getId();

        if (assignee != null) {

            // Get User Profile from User Management
            IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
            User user = identityService.createUserQuery().userId(assignee).singleResult();

            if (user != null) {

                // Get Email Address from User Profile
                String recipient = user.getEmail();

                if (recipient != null && !recipient.isEmpty()) {

                    Email email = new SimpleEmail();
                    email.setCharset("utf-8");
                    email.setHostName(HOST);
                    email.setSSLOnConnect(false);
                    email.setStartTLSRequired(true);
                    email.setStartTLSEnabled(true);
                    email.setSmtpPort(PORT);
                    email.setSslSmtpPort("587");

                    email.setAuthentication(USER, PWD);

                    try {
                        email.setFrom("promsystem@isc.tuc.gr");
                        email.setSubject("Ανάθεση Εργασίας: " + delegateTask.getName());
                        email.setMsg("Η Διαδικασία Διαλογής Ειδών Υπολογιστικής Υποδομής (Η/Υ και Περιφερειακά) ολοκληρώθηκε. " +
                                    "Παρακαλούμε ακολουθήστε την παρακάτω διεύθυνση προκειμένου να λάβετε και ακολούθως να υποβάλλετε την Λίστα Παραγγελιών. " +
                                    "Για την προμήθεια των εν λόγων Ειδών θα ακολουθήσει Διαγωνιστική Διαδικασία, συνολική για όλο το Ίδρυμα. " +
                                    "http://147.27.70.60:8080/camunda/app/tasklist/default/#/task/" + taskId);

                        email.addTo(recipient);

                        email.send();
                        LOGGER.info("Final Task Assignment Email successfully sent to user '" + assignee + "' with address '" + recipient + "'.");

                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
                    }

                } else {
                    LOGGER.warning("Not sending email to user " + assignee + "', user has no email address.");
                }

            } else {
                LOGGER.warning("Not sending email to user " + assignee + "', user is not enrolled with identity service.");
            }

        }

    }

}
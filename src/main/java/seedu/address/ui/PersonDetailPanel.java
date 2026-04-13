package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;

/**
 * Panel that displays the full details of a selected {@code Person}.
 */
public class PersonDetailPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailPanel.fxml";
    private static final String EMPTY_DISPLAY_VALUE = "N/A";

    private Person currentPerson;

    @FXML
    private StackPane detailPanelRoot;
    @FXML
    private VBox placeholderView;
    @FXML
    private VBox personDetailView;
    @FXML
    private Label name;
    @FXML
    private Label gender;
    @FXML
    private Label dob;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label gymLocation;
    @FXML
    private Label rate;
    @FXML
    private Label plan;
    @FXML
    private Label height;
    @FXML
    private Label weight;
    @FXML
    private Label bodyFat;
    @FXML
    private Label status;
    @FXML
    private Label address;
    @FXML
    private Label note;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonDetailPanel} showing the placeholder message.
     */
    public PersonDetailPanel() {
        super(FXML);
        configureValueLabelsForHorizontalScroll();
    }

    /**
     * Displays the full details of the given {@code Person}.
     */
    public void displayPerson(Person person) {
        requireNonNull(person);
        this.currentPerson = person;

        updateDetailPanelFields(person);
        updateTagDisplay(person);
        setPlaceholderVisible(false);
    }

    /**
     * Clears the displayed person and shows the placeholder message.
     */
    public void clearPerson() {
        this.currentPerson = null;
        setPlaceholderVisible(true);
    }

    /**
     * Returns the person currently being displayed, or {@code null} if none.
     */
    public Person getCurrentPerson() {
        return currentPerson;
    }

    private void configureValueLabelsForHorizontalScroll() {
        Label[] valueLabels = {name, gender, dob, phone, email, gymLocation, rate, plan, height,
            weight, bodyFat, status, address, note};

        for (Label valueLabel : valueLabels) {
            valueLabel.setMinWidth(Region.USE_PREF_SIZE);
            valueLabel.setTextOverrun(OverrunStyle.CLIP);
        }
    }

    private void updateDetailPanelFields(Person person) {
        name.setText(person.getName().fullName);
        gender.setText(person.getGender().value.toString());
        dob.setText(person.getDateOfBirth().toString());
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        gymLocation.setText(defaultIfBlank(person.getLocation().value));
        address.setText(person.getAddress().value);
        rate.setText(defaultIfBlank(person.getRate().value));
        plan.setText(person.getPlan().isUnassigned() ? EMPTY_DISPLAY_VALUE : person.getPlan().toString());
        height.setText(defaultIfBlank(person.getHeight().value));
        weight.setText(defaultIfBlank(person.getWeight().value));
        bodyFat.setText(defaultIfBlank(person.getBodyFatPercentage().value));
        status.setText(person.getStatus().toString());
        note.setText(defaultIfBlank(person.getNote().value));
    }

    private void updateTagDisplay(Person person) {
        tags.getChildren().clear();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    private void setPlaceholderVisible(boolean shouldShowPlaceholder) {
        placeholderView.setVisible(shouldShowPlaceholder);
        placeholderView.setManaged(shouldShowPlaceholder);
        personDetailView.setVisible(!shouldShowPlaceholder);
        personDetailView.setManaged(!shouldShowPlaceholder);
    }

    private String defaultIfBlank(String value) {
        return value.isEmpty() ? EMPTY_DISPLAY_VALUE : value;
    }
}

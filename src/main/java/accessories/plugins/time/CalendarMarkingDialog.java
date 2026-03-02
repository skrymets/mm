package accessories.plugins.time;

import accessories.plugins.time.JTripleCalendar.JSwitchableCalendar;
import freemind.controller.actions.CalendarMarking;
import freemind.controller.actions.CalendarMarkings;
import freemind.controller.actions.WindowConfigurationStorage;
import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.main.ColorUtils;
import freemind.modes.mindmapmode.MindMapController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
@Slf4j
public class CalendarMarkingDialog extends JDialog implements ActionListener, ChangeListener, PropertyChangeListener {

    private static final String WINDOW_PREFERENCE_STORAGE_PROPERTY = "CalendarMarkingDialog_WindowPosition";
    public static final int CANCEL = -1;

    public static final int OK = 1;

    private final MindMapController mController;
    private JPanel jContentPane;
    private JButton jOKButton;
    private JButton jCancelButton;
    /**
     * -- GETTER --
     *
     */
    @Getter
    private int result = CANCEL;
    private JSwitchableCalendar startDate;
    private JSwitchableCalendar endDate;
    private JColorChooser markerColor;
    private JComboBox<String> repetitionType;
    private JTextField nameField;
    private SpinnerNumberModel mRepeatEachNOccurenceModel;
    private SpinnerNumberModel mFirstOccurenceModel;
    private JTextArea mTextArea;
    private boolean mStarted = false;

    public static void main(String[] args) {
        CalendarMarkingDialog dialog = new CalendarMarkingDialog(null);
        String MARKINGS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><calendar_markings><calendar_marking name=\"bla\" color=\"#cc0099\" start_date=\"1443650400000\" end_date=\"1447801200000\" repeat_type=\"yearly\" repeat_each_n_occurence=\"1\" first_occurence=\"2\"/></calendar_markings>";
        CalendarMarkings markings = (CalendarMarkings) Tools.unMarshall(MARKINGS);
        dialog.setCalendarMarking(markings.getCalendarMarking(0));
        dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        dialog.setVisible(true);
        log.debug("Dialog result: {}", dialog.getResult());
        CalendarMarking marking = dialog.getCalendarMarking();
        CalendarMarkings markingsZwo = new CalendarMarkings();
        markingsZwo.addCalendarMarking(marking);
        log.debug("Markings XML: {}", Tools.marshall(markingsZwo));
    }

    public CalendarMarkingDialog(MindMapController pController) {
        mController = pController;
        setTitle(pController.getText("CalendarMarkingDialog.title"));
        JPanel contentPane = getJContentPane();
        this.setContentPane(contentPane);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                cancelPressed();
            }
        });
        Action cancelAction = new AbstractAction() {

            public void actionPerformed(ActionEvent arg0) {
                cancelPressed();
            }
        };
        SwingUtils.addEscapeActionToDialog(this, cancelAction);
        this.pack();
        if (mController != null) {
            mController.decorateDialog(this, WINDOW_PREFERENCE_STORAGE_PROPERTY);
        }
        mStarted = true;
    }

    private void close() {
        if (mController != null) {
            WindowConfigurationStorage storage = new WindowConfigurationStorage();
            mController.storeDialogPositions(this, storage,
                    WINDOW_PREFERENCE_STORAGE_PROPERTY);
        }
        this.dispose();

    }

    private void okPressed() {
        result = OK;
        // writePatternBackToModel();
        close();
    }

    private void cancelPressed() {
        result = CANCEL;
        close();
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            GroupLayout layout = new GroupLayout(jContentPane);
            jContentPane.setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            JLabel nameLabel = getLabel("Name");
            nameField = new JTextField(80);
            JLabel repetitionTypeLabel = getLabel("Repetition_Type");
            mRepetitionTypesList = new ArrayList<>();
            mRepetitionTypesList.add(("never"));
            mRepetitionTypesList.add(("yearly"));
            mRepetitionTypesList.add(("yearly_every_nth_day"));
            mRepetitionTypesList.add(("yearly_every_nth_week"));
            mRepetitionTypesList.add(("yearly_every_nth_month"));
            mRepetitionTypesList.add(("monthly"));
            mRepetitionTypesList.add(("monthly_every_nth_day"));
            mRepetitionTypesList.add(("monthly_every_nth_week"));
            mRepetitionTypesList.add(("weekly"));
            mRepetitionTypesList.add(("weekly_every_nth_day"));
            mRepetitionTypesList.add(("daily"));
            List<String> items = new ArrayList<>();
            for (String xmlName : mRepetitionTypesList) {
                items.add(getText(xmlName));
            }
            repetitionType = new JComboBox<>(items.toArray(new String[0]));
            JLabel repeatEachNOccurenceLabel = getLabel("Repeat_Each_N_Occurence");

            mRepeatEachNOccurenceModel = new SpinnerNumberModel(1, 1, 100, 1);
            JSpinner repeatEachNOccurence = new JSpinner(mRepeatEachNOccurenceModel);
            JLabel firstOccurenceLabel = getLabel("First_Occurence");
            mFirstOccurenceModel = new SpinnerNumberModel(0, 0, 100, 1);
            JSpinner firstOccurence = new JSpinner(mFirstOccurenceModel);
            JLabel startDateLabel = getLabel("Start_Date");
            startDate = new JSwitchableCalendar();
            startDate.setEnabled(true);
            JLabel endDateLabel = getLabel("End_Date");
            endDate = new JSwitchableCalendar();
            endDate.setEnabled(true);
            JLabel markerColorLabel = getLabel("Background_Color");
            markerColor = new JColorChooser();
            JButton okButton = getJOKButton();
            JButton cancelButton = getJCancelButton();
            JLabel examplesLabel = getLabel("CalendarMarkings.Examples");
            mTextArea = new JTextArea();
            mTextArea.setEditable(false);
            layout.setHorizontalGroup(
                    layout.createSequentialGroup().addGroup(layout.createParallelGroup()
                            .addComponent(nameLabel)
                            .addComponent(repetitionTypeLabel)
                            .addComponent(repeatEachNOccurenceLabel)
                            .addComponent(firstOccurenceLabel)
                            .addComponent(startDateLabel)
                            .addComponent(endDateLabel)
                            .addComponent(markerColorLabel)
                            .addComponent(examplesLabel)
                            .addComponent(okButton)
                    ).addGroup(layout.createParallelGroup()
                            .addComponent(nameField)
                            .addComponent(repetitionType)
                            .addComponent(repeatEachNOccurence)
                            .addComponent(firstOccurence)
                            .addComponent(startDate)
                            .addComponent(endDate)
                            .addComponent(markerColor)
                            .addComponent(mTextArea)
                            .addComponent(cancelButton)
                    )
            );
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup().addComponent(nameLabel).addComponent(nameField))
                            .addGroup(layout.createParallelGroup().addComponent(repetitionTypeLabel).addComponent(repetitionType))
                            .addGroup(layout.createParallelGroup().addComponent(repeatEachNOccurenceLabel).addComponent(repeatEachNOccurence))
                            .addGroup(layout.createParallelGroup().addComponent(firstOccurenceLabel).addComponent(firstOccurence))
                            .addGroup(layout.createParallelGroup().addComponent(startDateLabel).addComponent(startDate))
                            .addGroup(layout.createParallelGroup().addComponent(endDateLabel).addComponent(endDate))
                            .addGroup(layout.createParallelGroup().addComponent(markerColorLabel).addComponent(markerColor))
                            .addGroup(layout.createParallelGroup().addComponent(examplesLabel).addComponent(mTextArea))
                            .addGroup(layout.createParallelGroup().addComponent(okButton).addComponent(cancelButton))
            );

            getRootPane().setDefaultButton(okButton);
            repetitionType.addActionListener(this);
            repeatEachNOccurence.addChangeListener(this);
            firstOccurence.addChangeListener(this);
            startDate.addPropertyChangeListener(this);
            endDate.addPropertyChangeListener(this);
        }
        return jContentPane;
    }

    private JLabel getLabel(String pString) {
        JLabel label = new JLabel(getText(pString));
        label.setToolTipText(getText(pString + "_description"));
        return label;
    }

    public CalendarMarking getCalendarMarking() {
        CalendarMarking marking = new CalendarMarking();
        marking.setName(nameField.getText());
        marking.setColor(ColorUtils.colorToXml(markerColor.getColor()));
        marking.setStartDate(startDate.getCalendar().getTimeInMillis());
        marking.setEndDate(endDate.getCalendar().getTimeInMillis());
        marking.setFirstOccurence(mFirstOccurenceModel.getNumber().intValue());
        marking.setRepeatEachNOccurence(mRepeatEachNOccurenceModel.getNumber().intValue());
        int selectedIndex = repetitionType.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= mRepetitionTypesList.size()) {
            log.error("Selected combo box index out of range: {}", selectedIndex);
        } else {
            marking.setRepeatType(CalendarMarking.RepeatType.convert(mRepetitionTypesList.get(selectedIndex)));
        }
        return marking;
    }

    public void setCalendarMarking(CalendarMarking pMarking) {
        nameField.setText(pMarking.getName());
        markerColor.setColor(ColorUtils.xmlToColor(pMarking.getColor()));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(pMarking.getStartDate());
        startDate.setCalendar(cal);
        cal.setTimeInMillis(pMarking.getEndDate());
        endDate.setCalendar(cal);
        mFirstOccurenceModel.setValue(pMarking.getFirstOccurence());
        mRepeatEachNOccurenceModel.setValue(pMarking.getRepeatEachNOccurence());
        String repeatTypeString = pMarking.getRepeatType().xmlValue();
        if (mRepetitionTypesList.contains(repeatTypeString)) {
            repetitionType.setSelectedIndex(mRepetitionTypesList.indexOf(repeatTypeString));
        } else {
            log.error("Repetition type {} not found.", repeatTypeString);
            repetitionType.setSelectedIndex(0);
        }
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJOKButton() {
        if (jOKButton == null) {
            jOKButton = new JButton();

            jOKButton.setAction(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    okPressed();
                }

            });

            SwingUtils.setLabelAndMnemonic(jOKButton, getText("ok"));
        }
        return jOKButton;
    }

    public String getText(String textId) {
        if (mController != null) {
            return mController.getText(textId);
        }
        return textId;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getJCancelButton() {
        if (jCancelButton == null) {
            jCancelButton = new JButton();
            jCancelButton.setAction(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    cancelPressed();
                }
            });
            SwingUtils.setLabelAndMnemonic(jCancelButton,
                    getText(("cancel")));
        }
        return jCancelButton;
    }

    @Override
    public void actionPerformed(ActionEvent pE) {
        showExamples();
    }

    @Override
    public void stateChanged(ChangeEvent pE) {
        showExamples();
    }

    boolean ignoreNextEvent = false;
    private List<String> mRepetitionTypesList;

    @Override
    public void propertyChange(PropertyChangeEvent pEvt) {
        if (pEvt.getNewValue() instanceof Calendar && pEvt.getSource() instanceof JSwitchableCalendar) {
            if (!ignoreNextEvent) {
                Calendar cal = (Calendar) pEvt.getNewValue();
                ignoreNextEvent = true;
                ((JSwitchableCalendar) pEvt.getSource()).setCalendar(cal);
            } else {
                ignoreNextEvent = false;
            }
        }
        showExamples();
    }

    private void showExamples() {
        if (!mStarted) {
            return;
        }
        CalendarMarking marking = getCalendarMarking();
        CalendarMarkings container = new CalendarMarkings();
        container.addCalendarMarking(marking);
        CalendarMarkingEvaluator evaluator = new CalendarMarkingEvaluator(container);
        Set<Calendar> nEntries = evaluator.getAtLeastTheFirstNEntries(10);
        StringBuilder text = new StringBuilder();
        for (Calendar calendar : nEntries) {
            text.append(DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime())).append("\n");
        }
        mTextArea.setText(text.toString());
    }

    /**
     * Sets the dates of both start and end to the specified.
     *
     */
    public void setDates(Calendar pCal) {
        endDate.setDate(pCal);
        startDate.setDate(pCal);
    }

}

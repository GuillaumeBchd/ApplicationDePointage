package vue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JLayeredPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;

import javax.swing.JFormattedTextField;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable;
import javax.swing.JTextField;

import controleur.ListFilter;
import controleur.DataCentral;
import controleur.DateTimeTools;
import controleur.ReadWriteCentral;
import controleur.TCP.CentralClient;
import controleur.Tables.CheckInOutTable;
import controleur.Tables.DepartmentsTable;
import controleur.Tables.EmployeesTable;
import controleur.Tables.EventTable;
import controleur.Tables.IncoherencesTable;
import mains.Central;
import modele.Event;
import modele.Incoherence;
import modele.Parameters;
import modele.Actors.Employee;
import modele.Actors.Manager;
import modele.Administrative.Department;
import modele.TimeRelated.CheckInOut;
import modele.TimeRelated.WorkingDay;

import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

/**
 * Cette classe définit toute l'interface de la centrale, ainsi que ses boutons et leurs fonctionnement.
 *
 */
public class CentralInterface {
	
	/* =====================Attributs===================== */
	
	//Définition des couleurs
	private final Color colorUnselected = new Color(41, 101, 138);
	private final Color colorSelected = new Color(45, 156, 202);
	private final Color colorHover = new Color(169, 171, 184);
	private final Color colorTopMenu = new Color(37, 39, 77);
	private final Color colorSideMenu = new Color(37, 39, 77);
	private final Color colorTextField = Color.WHITE;
	private final Color colorBody = new Color(255, 255, 255);

	//Entiers utilisés pour définir quelle Frame doit être affichée dans le corps de la fenêtre
	private static final int FRAME_EMPLOYEES = 0;
    private static final int FRAME_CHECKINOUT = 1;
    private static final int FRAME_DEPARTMENTS = 2;
    private static final int FRAME_EVENTS = 3;
    private static final int FRAME_INCOHERENCES = 4;
    private int selectedFrame = FRAME_EMPLOYEES;
    
    //Entiers et variables utilisés pour définir quel filtre doit être utilisé pour afficher les pointages ou les employés et d'après quels paramètres
    private static final int CHECKINOUT_FILTER_TIME = 5;
    private static LocalDateTime checkInOutFilterStartDate = LocalDateTime.now().withHour(8).withMinute(0).truncatedTo(ChronoUnit.MINUTES);
    private static LocalDateTime checkInOutFilterEndDate = LocalDateTime.now().withHour(17).withMinute(0).truncatedTo(ChronoUnit.MINUTES);
    private static final int CHECKINOUT_FILTER_DATE = 6;
    private static LocalDate checkInOutFilterDate = LocalDate.now();
    private static final int CHECKINOUT_FILTER_DEPARTMENT = 7;
    private static int checkInOutFilterIdDepartment = 0;
    private static final int CHECKINOUT_FILTER_PERSON = 8;
    private static int checkInOutFilterIdPerson = 0;
    private static final int CHECKINOUT_FILTER_CURRENT_DAY = 9;
    private static int checkInOut_filter_type = -1;
    
    private static final int EMPLOYEE_FILTER_MANAGER = 10;
    private static final int EMPLOYEE_FILTER_DEPARTMENT = 11;
    private static int employeeFilterIdDepartment = -1;
    private static List<Employee> listTableEmployee = new ArrayList<Employee>();
    private static List<Employee> listTableEmployeeAfterResearch = new ArrayList<Employee>();
    private static int employee_filter_type = -1;
    
    //Définition des éléments de la fenêtre dont il faut accéder en dehors de la fonction principale de CentralInterface
	public JFrame mainFrame;
	private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private static JTable tableEmployee;
	private static EmployeesTable modelTableEmployee = new EmployeesTable(DataCentral.getEmployees());
	private static JTable tableCheckInOut;
	private static CheckInOutTable modelTableCheckInOut = new CheckInOutTable(DataCentral.getWorkingDays());
	private static JTable tableDepartments;
	private static DepartmentsTable modelTableDepartments = new DepartmentsTable(DataCentral.getDepartments());
	
	private static JButton buttonEvents;
	private static Color colorButtonEvents = new Color(41, 101, 138);
	private static JTable tableEvent;
	private static EventTable modelTableEvents = new EventTable(DataCentral.getEvents());
	
	private static JButton buttonIncoherences;
	private static Color colorButtonIncoherences = new Color(41, 101, 138);
	private static JTable tableIncoherences;
	private static IncoherencesTable modelTableIncoherences = new IncoherencesTable(DataCentral.getIncoherences());

	public static JLabel sideMenuTimeLabel;
	
	/**
	 * Méthode que syncPointeuseToCentrale appelle pour mettre à jour toutes les JTables de la centrale
	 */
	public static void refreshTables(){
		switch(employee_filter_type) {
			case EMPLOYEE_FILTER_MANAGER :
				listTableEmployee = new ArrayList<Employee>();
				if(DataCentral.getManagers()!=null) {
					for(Manager m : DataCentral.getManagers()) {
						listTableEmployee.add(m);
					}
				}
				break;
			case EMPLOYEE_FILTER_DEPARTMENT :
				listTableEmployee = new ArrayList<Employee>();
				if(DataCentral.getEmployees()!=null) {
					for(Employee e : DataCentral.getEmployees()) {
						if(e.getIdDepartment() == employeeFilterIdDepartment) {
							listTableEmployee.add(e);
						}
					}
				}
				if(DataCentral.getManagers()!=null) {
					for(Manager m : DataCentral.getManagers()) {
						if(m.getIdDepartment() == employeeFilterIdDepartment) {
							listTableEmployee.add(m);
						}
					}
				}
				break;
			default:
				listTableEmployee = new ArrayList<Employee>();
				if(DataCentral.getEmployees() != null) {
					for(Employee e : DataCentral.getEmployees()) {
						listTableEmployee.add(e);
					}
				}
				if(DataCentral.getManagers() != null) {
					for(Manager m : DataCentral.getManagers()) {
						listTableEmployee.add(m);
					}
				}
				break;
		}
		modelTableEmployee.setListEmployees(listTableEmployee);
		modelTableEmployee.fireTableDataChanged();
		
		modelTableCheckInOut.setWorkingDays(callCheckInOutFilter());
		modelTableCheckInOut.fireTableDataChanged();
		
		modelTableDepartments.setDepartments(DataCentral.getDepartments());
		modelTableDepartments.fireTableDataChanged();
		
		modelTableEvents.setEvents(DataCentral.getEvents());
		modelTableEvents.fireTableDataChanged();
		buttonEvents.setText("  Events (" + tableEvent.getRowCount() +")");
		if(tableEvent.getRowCount() > 0) {
			colorButtonEvents = new Color(176, 0, 32);
		}
		else {
			colorButtonEvents = new Color(41, 101, 138);
		}
		buttonEvents.setBackground(colorButtonEvents);
		
		modelTableIncoherences.setIncoherencesList(DataCentral.getIncoherences());
		modelTableIncoherences.fireTableDataChanged();
		buttonIncoherences.setText("  Incoherence (" + tableIncoherences.getRowCount() +")");
		if(tableIncoherences.getRowCount() > 0) {
			colorButtonIncoherences = new Color(176, 0, 32);
		}
		else {
			colorButtonIncoherences = new Color(41, 101, 138);
		}
		buttonIncoherences.setBackground(colorButtonIncoherences);
	}
	
	/**
	 * Fonction qui se charge d'appeler la bonne méthode du controleur et qui renvoie la liste de pointages correspondante
	 */
	public static List<WorkingDay> callCheckInOutFilter() {
		switch(checkInOut_filter_type) {
			case CHECKINOUT_FILTER_TIME :
				return ListFilter.checkInOutFilter(checkInOutFilterStartDate, checkInOutFilterEndDate);
			case CHECKINOUT_FILTER_DATE :
				return ListFilter.checkInOutFilter(checkInOutFilterDate);
			case CHECKINOUT_FILTER_DEPARTMENT :
				return ListFilter.checkInOutDepartmentFilter(checkInOutFilterIdDepartment);
			case CHECKINOUT_FILTER_PERSON :
				return ListFilter.checkInOutPersonFilter(checkInOutFilterIdPerson);
			case CHECKINOUT_FILTER_CURRENT_DAY :
				return ListFilter.checkInOutCurrentDay();
			default :
				return ListFilter.checkInOutNoFilter();
		}
	}
	
	/**
	 * Fenêtre popup pour sélectionner le filtre de l'affichage des employés.
	 * @param parent : La frame principale
	 */
	public static void dialogPanelFiltersEmployee(JFrame parent) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);

		JPanel titlePanel = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		JPanel panelManager = new JPanel();
        JPanel panelDepartment = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(titlePanel).addComponent(topPanel).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(titlePanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(topPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		
        GroupLayout topLayout = new GroupLayout(topPanel);
        topLayout.setAutoCreateGaps(true);
        topLayout.setAutoCreateContainerGaps(true);

        GroupLayout managerPanelLayout = new GroupLayout(panelManager);
        managerPanelLayout.setAutoCreateGaps(true);
        managerPanelLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout departmentPanelLayout = new GroupLayout(panelDepartment);
        departmentPanelLayout.setAutoCreateGaps(true);
        departmentPanelLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        //=============================FILTER SELECTION=============================
        JLabel filterTypeLabel = new JLabel("Filter type :");
        JRadioButton managerRadioButton = new JRadioButton("Managers");
        JRadioButton departmentRadioButton = new JRadioButton("Department");

        bodyPanel.add(panelManager);
        panelManager.setLayout(managerPanelLayout);
        
        bodyPanel.add(panelDepartment);
        panelDepartment.setLayout(departmentPanelLayout);
        
        managerRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelManager.setVisible(true);
            	panelDepartment.setVisible(false);
            	dialogPanel.pack();
            }
        });
        departmentRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelManager.setVisible(false);
            	panelDepartment.setVisible(true);
            	dialogPanel.pack();
            }
        });
        
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(managerRadioButton);
        radioButtonGroup.add(departmentRadioButton);
        managerRadioButton.setSelected(true);
        //First disposition with managerRadioButton selected :
        panelManager.setVisible(true);
    	panelDepartment.setVisible(false);
        
        titlePanel.add(filterTypeLabel);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        GroupLayout.SequentialGroup tophMainGroup = topLayout.createSequentialGroup();
        tophMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER)
    		.addComponent(managerRadioButton)
    	);
        tophMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER)
        	.addComponent(departmentRadioButton)
        );
        topLayout.setHorizontalGroup(tophMainGroup);
        
        GroupLayout.SequentialGroup topvMainGroup = topLayout.createSequentialGroup();
        topvMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER).addComponent(managerRadioButton).addComponent(departmentRadioButton));
        topLayout.setVerticalGroup(topvMainGroup);
        //===========================================================================
        
        
        //=======================Panel manager======================
        JLabel managerLabel = new JLabel("Show only managers");
        GroupLayout.SequentialGroup managerhGroup = managerPanelLayout.createSequentialGroup();
        managerhGroup.addGroup(managerPanelLayout.createParallelGroup().addComponent(managerLabel));
        managerPanelLayout.setHorizontalGroup(managerhGroup);
        
        GroupLayout.SequentialGroup managervGroup = managerPanelLayout.createSequentialGroup();
        managervGroup.addGroup(managerPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(managerLabel));
        managerPanelLayout.setVerticalGroup(managervGroup);
        panelManager.add(managerLabel);
        //==========================================================
        
        
        //=====================Panel department=====================
        JLabel departmentLabel = new JLabel("Department : ");
        JComboBox<Department> departmentList = new JComboBox<Department>();
        if(DataCentral.getDepartments()!=null) {
        	for(Department d : DataCentral.getDepartments()) {
            	departmentList.addItem(d);
            }
        }
        
        GroupLayout.SequentialGroup departmenthGroup = departmentPanelLayout.createSequentialGroup();
        departmenthGroup.addGroup(departmentPanelLayout.createParallelGroup().addComponent(departmentLabel));
        departmenthGroup.addGroup(departmentPanelLayout.createParallelGroup().addComponent(departmentList));
        departmentPanelLayout.setHorizontalGroup(departmenthGroup);
        
        GroupLayout.SequentialGroup departmentvGroup = departmentPanelLayout.createSequentialGroup();
        departmentvGroup.addGroup(departmentPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(departmentLabel).addComponent(departmentList));
        departmentPanelLayout.setVerticalGroup(departmentvGroup);
        //==========================================================
        
        JButton createButton = new JButton("Save");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(managerRadioButton.isSelected() == true) {
					employee_filter_type = EMPLOYEE_FILTER_MANAGER;

	            	refreshTables();
					
					dialogPanel.dispose();
				}
				if(departmentList.getSelectedItem() != null) {
					if(departmentRadioButton.isSelected() == true) {
						employee_filter_type = EMPLOYEE_FILTER_DEPARTMENT;
						
						employeeFilterIdDepartment = ((Department)departmentList.getSelectedItem()).getId();
						
		            	refreshTables();
					}
				}
				
				dialogPanel.dispose();
				
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Edit filters");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/settings.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenêtre popup pour sélectionner le filtre de l'affichage des pointages.
	 * @param parent : La frame principale
	 */
	@SuppressWarnings("unchecked")
	public static void dialogPanelFiltersCheckInOut(JFrame parent) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);

		JPanel titlePanel = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		JPanel panelTime = new JPanel();
        JPanel panelDate = new JPanel();
        JPanel panelDepartment = new JPanel();
        JPanel panelPerson = new JPanel();
        JPanel panelCurrentDay = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(titlePanel).addComponent(topPanel).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(titlePanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(topPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		
        GroupLayout topLayout = new GroupLayout(topPanel);
        topLayout.setAutoCreateGaps(true);
        topLayout.setAutoCreateContainerGaps(true);

        GroupLayout timePanelLayout = new GroupLayout(panelTime);
        timePanelLayout.setAutoCreateGaps(true);
        timePanelLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout datePanelLayout = new GroupLayout(panelDate);
        datePanelLayout.setAutoCreateGaps(true);
        datePanelLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout departmentPanelLayout = new GroupLayout(panelDepartment);
        departmentPanelLayout.setAutoCreateGaps(true);
        departmentPanelLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout personPanelLayout = new GroupLayout(panelPerson);
        personPanelLayout.setAutoCreateGaps(true);
        personPanelLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout currentDayPanelLayout = new GroupLayout(panelCurrentDay);
        currentDayPanelLayout.setAutoCreateGaps(true);
        currentDayPanelLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        //=============================FILTER SELECTION=============================
        JLabel filterTypeLabel = new JLabel("Filter type :");
        JRadioButton timeRadioButton = new JRadioButton("Time period");
        JRadioButton dateRadioButton = new JRadioButton("Date");
        JRadioButton departmentRadioButton = new JRadioButton("Department");
        JRadioButton personRadioButton = new JRadioButton("Person");
        JRadioButton currentDayRadioButton = new JRadioButton("Current day");

        bodyPanel.add(panelTime);
        panelTime.setLayout(timePanelLayout);
        bodyPanel.add(panelDate);
        panelDate.setLayout(datePanelLayout);
        bodyPanel.add(panelDepartment);
        panelDepartment.setLayout(departmentPanelLayout);
        bodyPanel.add(panelPerson);
        panelPerson.setLayout(personPanelLayout);
        bodyPanel.add(panelCurrentDay);
        panelCurrentDay.setLayout(currentDayPanelLayout);
        
        timeRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelTime.setVisible(true);
            	panelDate.setVisible(false);
            	panelDepartment.setVisible(false);
            	panelPerson.setVisible(false);
            	panelCurrentDay.setVisible(false);
            	dialogPanel.pack();
            }
        });
        dateRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelTime.setVisible(false);
            	panelDate.setVisible(true);
            	panelDepartment.setVisible(false);
            	panelPerson.setVisible(false);
            	panelCurrentDay.setVisible(false);
            	dialogPanel.pack();
            }
        });
        departmentRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelTime.setVisible(false);
            	panelDate.setVisible(false);
            	panelDepartment.setVisible(true);
            	panelPerson.setVisible(false);
            	panelCurrentDay.setVisible(false);
            	dialogPanel.pack();
            }
        });
        personRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelTime.setVisible(false);
            	panelDate.setVisible(false);
            	panelDepartment.setVisible(false);
            	panelPerson.setVisible(true);
            	panelCurrentDay.setVisible(false);
            	dialogPanel.pack();
            }
        });
        currentDayRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	panelTime.setVisible(false);
            	panelDate.setVisible(false);
            	panelDepartment.setVisible(false);
            	panelPerson.setVisible(false);
            	panelCurrentDay.setVisible(true);
            	dialogPanel.pack();
            }
        });
        
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(timeRadioButton);
        radioButtonGroup.add(dateRadioButton);
        radioButtonGroup.add(departmentRadioButton);
        radioButtonGroup.add(personRadioButton);
        radioButtonGroup.add(currentDayRadioButton);
        timeRadioButton.setSelected(true);
        //First disposition with timeRadioButton selected :
        panelTime.setVisible(true);
    	panelDate.setVisible(false);
    	panelDepartment.setVisible(false);
    	panelPerson.setVisible(false);
    	panelCurrentDay.setVisible(false);
        
        titlePanel.add(filterTypeLabel);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        GroupLayout.SequentialGroup tophMainGroup = topLayout.createSequentialGroup();
        tophMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER)
    		.addComponent(timeRadioButton)
    	);
        tophMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER)
    		.addComponent(dateRadioButton)
        );
        tophMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER)
        	.addComponent(departmentRadioButton)
        );
        tophMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER)
        	.addComponent(personRadioButton)
        );
        tophMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER)
            	.addComponent(currentDayRadioButton)
            );
        topLayout.setHorizontalGroup(tophMainGroup);
        
        GroupLayout.SequentialGroup topvMainGroup = topLayout.createSequentialGroup();
        topvMainGroup.addGroup(topLayout.createParallelGroup(Alignment.CENTER).addComponent(timeRadioButton).addComponent(dateRadioButton).addComponent(departmentRadioButton).addComponent(personRadioButton).addComponent(currentDayRadioButton));
        topLayout.setVerticalGroup(topvMainGroup);
        //==========================================================================
        
        //=============================TIME PERIOD=============================
        JLabel startDateLabel = new JLabel("Start date");
		JTextField startyearField = new JTextField(2);
		JTextField startmonthField = new JTextField(1);
		JTextField startdayField = new JTextField(1);
        JLabel startTimeLabel = new JLabel("Start time");
		JComboBox<Integer> startHourList = new JComboBox<Integer>();
		JComboBox<Integer> startMinuteList = new JComboBox<Integer>();
		JLabel endDateLabel = new JLabel("End date");
		JTextField endyearField = new JTextField(2);
		JTextField endmonthField = new JTextField(1);
		JTextField enddayField = new JTextField(1);
		JLabel endTimeLabel = new JLabel("End time");
		JComboBox<Integer> endHourList = new JComboBox<Integer>();
		JComboBox<Integer> endMinuteList = new JComboBox<Integer>();
		
        JPanel startDatePanel = new JPanel();
        GroupLayout startdateLayout = new GroupLayout(startDatePanel);
        startDatePanel.setLayout(startdateLayout);
        
        GroupLayout.SequentialGroup starthDateGroup = startdateLayout.createSequentialGroup();
        starthDateGroup.addGroup(startdateLayout.createParallelGroup().addComponent(startyearField));
        starthDateGroup.addGroup(startdateLayout.createParallelGroup().addComponent(startmonthField));
        starthDateGroup.addGroup(startdateLayout.createParallelGroup().addComponent(startdayField));
        startdateLayout.setHorizontalGroup(starthDateGroup);
        
        GroupLayout.SequentialGroup startvDateGroup = startdateLayout.createSequentialGroup();
        startvDateGroup.addGroup(startdateLayout.createParallelGroup(Alignment.CENTER).addComponent(startyearField).addComponent(startmonthField).addComponent(startdayField));
        startdateLayout.setVerticalGroup(startvDateGroup);
        
        JPanel startTimePanel = new JPanel();
        startTimePanel.add(startHourList);
        startTimePanel.add(new JLabel("h   "));
        startTimePanel.add(startMinuteList);
        startTimePanel.add(new JLabel("min"));
		
        JPanel endDatePanel = new JPanel();
        GroupLayout enddateLayout = new GroupLayout(endDatePanel);
        endDatePanel.setLayout(enddateLayout);
        
        GroupLayout.SequentialGroup endhDateGroup = enddateLayout.createSequentialGroup();
        endhDateGroup.addGroup(enddateLayout.createParallelGroup().addComponent(endyearField));
        endhDateGroup.addGroup(enddateLayout.createParallelGroup().addComponent(endmonthField));
        endhDateGroup.addGroup(enddateLayout.createParallelGroup().addComponent(enddayField));
        enddateLayout.setHorizontalGroup(endhDateGroup);
        
        GroupLayout.SequentialGroup endvDateGroup = enddateLayout.createSequentialGroup();
        endvDateGroup.addGroup(enddateLayout.createParallelGroup(Alignment.CENTER).addComponent(endyearField).addComponent(endmonthField).addComponent(enddayField));
        enddateLayout.setVerticalGroup(endvDateGroup);
        
        JPanel endTimePanel = new JPanel();
        endTimePanel.add(endHourList);
        endTimePanel.add(new JLabel("h   "));
        endTimePanel.add(endMinuteList);
        endTimePanel.add(new JLabel("min"));

		//On remplit les listes
        for(int iIndice=0; iIndice<=45; iIndice+=15){
        	startMinuteList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<=45; iIndice+=15){
        	endMinuteList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<24; iIndice++){
        	startHourList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<24; iIndice++){
        	endHourList.addItem(iIndice);
        }
        
		GroupLayout.SequentialGroup hGroup = timePanelLayout.createSequentialGroup();
        hGroup.addGroup(timePanelLayout.createParallelGroup()
    		.addComponent(startDateLabel)
    		.addComponent(startTimeLabel)
    		.addComponent(endDateLabel)
    		.addComponent(endTimeLabel)
        );
        hGroup.addGroup(timePanelLayout.createParallelGroup()
        		.addComponent(startDatePanel)
            	.addComponent(startTimePanel)
        		.addComponent(endDatePanel)
        		.addComponent(endTimePanel)
        );
        timePanelLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = timePanelLayout.createSequentialGroup();
        vGroup.addGroup(timePanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(startDateLabel).addComponent(startDatePanel));
        vGroup.addGroup(timePanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(startTimeLabel).addComponent(startTimePanel));
        vGroup.addGroup(timePanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(endDateLabel).addComponent(endDatePanel));
        vGroup.addGroup(timePanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(endTimeLabel).addComponent(endTimePanel));
        timePanelLayout.setVerticalGroup(vGroup);
        
        //Selectionner les valeurs précédentes :
        startyearField.setText(Integer.toString(checkInOutFilterStartDate.getYear()));
        startmonthField.setText(Integer.toString(checkInOutFilterStartDate.getMonthValue()));
        startdayField.setText(Integer.toString(checkInOutFilterStartDate.getDayOfMonth()));
        
    	startHourList.setSelectedIndex(checkInOutFilterStartDate.getHour());
    	switch(checkInOutFilterStartDate.getMinute()) {
    	case 0:
    		startMinuteList.setSelectedIndex(0);
    		break;
    	case 15:
    		startMinuteList.setSelectedIndex(1);
    		break;
    	case 30:
    		startMinuteList.setSelectedIndex(2);
    		break;
    	case 45:
    		startMinuteList.setSelectedIndex(3);
    		break;
		default :
			startMinuteList.setSelectedIndex(0);
    		break;
    	}
    	
    	endyearField.setText(Integer.toString(checkInOutFilterEndDate.getYear()));
        endmonthField.setText(Integer.toString(checkInOutFilterEndDate.getMonthValue()));
        enddayField.setText(Integer.toString(checkInOutFilterEndDate.getDayOfMonth()));
        
    	endHourList.setSelectedIndex(checkInOutFilterEndDate.getHour());
    	switch(checkInOutFilterEndDate.getMinute()) {
    	case 0:
    		endMinuteList.setSelectedIndex(0);
    		break;
    	case 15:
    		endMinuteList.setSelectedIndex(1);
    		break;
    	case 30:
    		endMinuteList.setSelectedIndex(2);
    		break;
    	case 45:
    		endMinuteList.setSelectedIndex(3);
    		break;
		default :
    		endMinuteList.setSelectedIndex(0);
    		break;
    	}
        //==========================================================
        
        
        //=============================DATE=============================
        JLabel dateLabel = new JLabel("Date : ");
        JTextField yearField = new JTextField(4);
        yearField.setText(Integer.toString(checkInOutFilterDate.getYear()));
		JTextField monthField = new JTextField(2);
		monthField.setText(Integer.toString(checkInOutFilterDate.getMonthValue()));
		JTextField dayField = new JTextField(2);
		dayField.setText(Integer.toString(checkInOutFilterDate.getDayOfMonth()));
		GroupLayout.SequentialGroup dateFilterhMainGroup = datePanelLayout.createSequentialGroup();
		dateFilterhMainGroup.addGroup(datePanelLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(dateLabel)
		);
		dateFilterhMainGroup.addGroup(datePanelLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(yearField)
		);
		dateFilterhMainGroup.addGroup(datePanelLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(monthField)
		);
		dateFilterhMainGroup.addGroup(datePanelLayout.createParallelGroup(Alignment.CENTER)
				.addComponent(dayField)
		);
		datePanelLayout.setHorizontalGroup(dateFilterhMainGroup);
		
		GroupLayout.SequentialGroup dateFiltervMainGroup = datePanelLayout.createSequentialGroup();
		dateFiltervMainGroup.addGroup(datePanelLayout.createParallelGroup(Alignment.CENTER).addComponent(dateLabel).addComponent(yearField).addComponent(monthField).addComponent(dayField));
		datePanelLayout.setVerticalGroup(dateFiltervMainGroup);
        //==========================================================
        
        
        //=============================DEPARTMENT=============================
        JLabel departmentLabel = new JLabel("Department : ");
        JComboBox<Department> departmentList = new JComboBox<Department>();
        for(Department d : DataCentral.getDepartments()) {
        	departmentList.addItem(d);
        }
        GroupLayout.SequentialGroup departmenthGroup = departmentPanelLayout.createSequentialGroup();
        departmenthGroup.addGroup(departmentPanelLayout.createParallelGroup().addComponent(departmentLabel));
        departmenthGroup.addGroup(departmentPanelLayout.createParallelGroup().addComponent(departmentList));
        departmentPanelLayout.setHorizontalGroup(departmenthGroup);
        
        GroupLayout.SequentialGroup departmentvGroup = departmentPanelLayout.createSequentialGroup();
        departmentvGroup.addGroup(departmentPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(departmentLabel).addComponent(departmentList));
        departmentPanelLayout.setVerticalGroup(departmentvGroup);
        //====================================================================
        
        
        //=============================PERSON=============================
        JLabel personLabel = new JLabel("Person : ");
        @SuppressWarnings("rawtypes")
		JComboBox personList = new JComboBox();
        for(Employee e : DataCentral.getEmployees()) {
        	personList.addItem(e);
        }
        for(Manager m : DataCentral.getManagers()) {
        	personList.addItem(m);
        }
        GroupLayout.SequentialGroup personhGroup = personPanelLayout.createSequentialGroup();
        personhGroup.addGroup(personPanelLayout.createParallelGroup().addComponent(personLabel));
        personhGroup.addGroup(personPanelLayout.createParallelGroup().addComponent(personList));
        personPanelLayout.setHorizontalGroup(personhGroup);
        
        GroupLayout.SequentialGroup personvGroup = personPanelLayout.createSequentialGroup();
        personvGroup.addGroup(personPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(personLabel).addComponent(personList));
        personPanelLayout.setVerticalGroup(personvGroup);
        //================================================================
        
        //=============================CURRENT DAY=============================
        JLabel currentDayLabel = new JLabel("<html><center>Display only current day check in/out<br>(or incomplete working days)</center></html>");
        
        GroupLayout.SequentialGroup currentDayhGroup = currentDayPanelLayout.createSequentialGroup();
        currentDayhGroup.addGroup(currentDayPanelLayout.createParallelGroup().addComponent(currentDayLabel));
        currentDayPanelLayout.setHorizontalGroup(currentDayhGroup);
        
        GroupLayout.SequentialGroup currentDayvGroup = currentDayPanelLayout.createSequentialGroup();
        currentDayvGroup.addGroup(currentDayPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(currentDayLabel));
        currentDayPanelLayout.setVerticalGroup(currentDayvGroup);
        //====================================================================
        
		JButton createButton = new JButton("Save");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(timeRadioButton.isSelected() == true) {
					boolean error = false;
					try {
						Integer.parseInt(startyearField.getText());
						Integer.parseInt(startmonthField.getText());
						Integer.parseInt(startdayField.getText());
						Integer.parseInt(endyearField.getText());
						Integer.parseInt(endmonthField.getText());
						Integer.parseInt(enddayField.getText());
					}catch(java.lang.NumberFormatException e) {
						error = true;
						JOptionPane.showMessageDialog(parent, "Please enter integers only", "Unable to create filters", JOptionPane.ERROR_MESSAGE);
					}
					if(error == false) {
						try {
							checkInOutFilterStartDate = LocalDateTime.now()
									.withYear(Integer.parseInt(startyearField.getText()))
									.withMonth(Integer.parseInt(startmonthField.getText()))
									.withDayOfMonth(Integer.parseInt(startdayField.getText()))
									.withHour((int)startHourList.getSelectedItem())
									.withMinute((int)startMinuteList.getSelectedItem());
						    checkInOutFilterEndDate = LocalDateTime.now()
									.withYear(Integer.parseInt(endyearField.getText()))
									.withMonth(Integer.parseInt(endmonthField.getText()))
									.withDayOfMonth(Integer.parseInt(enddayField.getText()))
									.withHour((int)endHourList.getSelectedItem())
									.withMinute((int)endMinuteList.getSelectedItem());
						    
			            	checkInOut_filter_type = CHECKINOUT_FILTER_TIME;

			            	refreshTables();
							
							dialogPanel.dispose();
						}
						catch(java.time.DateTimeException e) {
							JOptionPane.showMessageDialog(parent, "Please enter valid dates", "Unable to create filters", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				if(dateRadioButton.isSelected() == true) {
					boolean error = false;
					try {
						Integer.parseInt(yearField.getText());
						Integer.parseInt(monthField.getText());
						Integer.parseInt(dayField.getText());
					}catch(java.lang.NumberFormatException e) {
						error = true;
						JOptionPane.showMessageDialog(parent, "Please enter integers only", "Unable to create filters", JOptionPane.ERROR_MESSAGE);
					}
					if(error == false) {
						try{
							checkInOutFilterDate = LocalDate.now().withYear(Integer.parseInt(yearField.getText())).withMonth(Integer.parseInt(monthField.getText())).withDayOfMonth(Integer.parseInt(dayField.getText()));
						
							checkInOut_filter_type = CHECKINOUT_FILTER_DATE;
			            	
			            	refreshTables();
							
							dialogPanel.dispose();
						}
						catch(java.time.DateTimeException e) {
							JOptionPane.showMessageDialog(parent, "Please enter a valid date", "Unable to create filters", JOptionPane.ERROR_MESSAGE);
						}
		            	
					}
				}
				if(departmentRadioButton.isSelected() == true) {
					if(departmentList.getSelectedItem() != null) {
						checkInOutFilterIdDepartment =  ((Department)departmentList.getSelectedItem()).getId();
						
		            	checkInOut_filter_type = CHECKINOUT_FILTER_DEPARTMENT;
		            	
		            	refreshTables();
					}
					
					dialogPanel.dispose();
				}
				if(personRadioButton.isSelected() == true) {
					if(personList.getSelectedItem() != null) {
						checkInOutFilterIdPerson = ((Employee)personList.getSelectedItem()).getIdentifiant();
						
		            	checkInOut_filter_type = CHECKINOUT_FILTER_PERSON;
		            	
		            	refreshTables();
					}
					
					dialogPanel.dispose();
				}
				if(currentDayRadioButton.isSelected() == true) {
					checkInOut_filter_type = CHECKINOUT_FILTER_CURRENT_DAY;
	            	
	            	refreshTables();
					
					dialogPanel.dispose();
				}
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Edit filters");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/settings.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenetre popup affichee lorsque l'on clique sur "Modify CheckInOut"
	 * @param parent : La frame principale
	 * @param typeCheckInOut 1=WorkingDay in employee, 2=WorkingDay in manager and 3=WorkingDay in DataCentral
	 * @param emp : L'employé correspondant au jour de travail
	 * @param workingDayToModify : Le jour de travail qui a été sélectionné pour modification
	 */
	public static void dialogPanelModifyCheckInOut(JFrame parent, int typeCheckInOut, Employee emp, WorkingDay workingDayToModify) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
		
		JLabel employeeLabel = new JLabel("Employee");
        JComboBox<Employee> employeeList = new JComboBox<Employee>();
        
        JLabel startDateLabel = new JLabel("Start date");
		JTextField startyearField = new JTextField(2);
		JTextField startmonthField = new JTextField(1);
		JTextField startdayField = new JTextField(1);
		
        JPanel startDatePanel = new JPanel();
        GroupLayout startdateLayout = new GroupLayout(startDatePanel);
        startDatePanel.setLayout(startdateLayout);
        
        GroupLayout.SequentialGroup starthDateGroup = startdateLayout.createSequentialGroup();
        starthDateGroup.addGroup(startdateLayout.createParallelGroup().addComponent(startyearField));
        starthDateGroup.addGroup(startdateLayout.createParallelGroup().addComponent(startmonthField));
        starthDateGroup.addGroup(startdateLayout.createParallelGroup().addComponent(startdayField));
        startdateLayout.setHorizontalGroup(starthDateGroup);
        
        GroupLayout.SequentialGroup startvDateGroup = startdateLayout.createSequentialGroup();
        startvDateGroup.addGroup(startdateLayout.createParallelGroup(Alignment.CENTER).addComponent(startyearField).addComponent(startmonthField).addComponent(startdayField));
        startdateLayout.setVerticalGroup(startvDateGroup);
        
        JLabel startTimeLabel = new JLabel("Start time");
		JComboBox<Integer> startHourList = new JComboBox<Integer>();
		JComboBox<Integer> startMinuteList = new JComboBox<Integer>();
        JPanel startTimePanel = new JPanel();
        startTimePanel.add(startHourList);
        startTimePanel.add(new JLabel("h   "));
        startTimePanel.add(startMinuteList);
        startTimePanel.add(new JLabel("min"));
        
        //=====end=====
        
        JLabel endDateLabel = new JLabel("End date");
		JTextField endyearField = new JTextField(2);
		JTextField endmonthField = new JTextField(1);
		JTextField enddayField = new JTextField(1);
		
        JPanel endDatePanel = new JPanel();
        GroupLayout enddateLayout = new GroupLayout(endDatePanel);
        endDatePanel.setLayout(enddateLayout);
        
        GroupLayout.SequentialGroup endhDateGroup = enddateLayout.createSequentialGroup();
        endhDateGroup.addGroup(enddateLayout.createParallelGroup().addComponent(endyearField));
        endhDateGroup.addGroup(enddateLayout.createParallelGroup().addComponent(endmonthField));
        endhDateGroup.addGroup(enddateLayout.createParallelGroup().addComponent(enddayField));
        enddateLayout.setHorizontalGroup(endhDateGroup);
        
        GroupLayout.SequentialGroup endvDateGroup = enddateLayout.createSequentialGroup();
        endvDateGroup.addGroup(enddateLayout.createParallelGroup(Alignment.CENTER).addComponent(endyearField).addComponent(endmonthField).addComponent(enddayField));
        enddateLayout.setVerticalGroup(endvDateGroup);
        
        JLabel endTimeLabel = new JLabel("End time");
		JComboBox<Integer> endHourList = new JComboBox<Integer>();
		JComboBox<Integer> endMinuteList = new JComboBox<Integer>();
        JPanel endTimePanel = new JPanel();
        endTimePanel.add(endHourList);
        endTimePanel.add(new JLabel("h   "));
        endTimePanel.add(endMinuteList);
        endTimePanel.add(new JLabel("min"));

		//Fill the lists
        for(Employee e : DataCentral.getEmployees()){
        	employeeList.addItem(e);
        }
        for(Manager m : DataCentral.getManagers()) {
			employeeList.addItem((Employee) m);
		}
        
        for(int iIndice=0; iIndice<=45; iIndice+=15){
        	startMinuteList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<=45; iIndice+=15){
        	endMinuteList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<24; iIndice++){
        	startHourList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<24; iIndice++){
        	endHourList.addItem(iIndice);
        }
        
        //Pre-select the correct elements
        employeeList.setSelectedItem(emp);
        
        if(workingDayToModify.getStart() != null) {
        	startyearField.setText(Integer.toString(workingDayToModify.getStart().getRoundedDateTime().getYear()));
            startmonthField.setText(Integer.toString(workingDayToModify.getStart().getRoundedDateTime().getMonthValue()));
            startdayField.setText(Integer.toString(workingDayToModify.getStart().getRoundedDateTime().getDayOfMonth()));
            
        	startHourList.setSelectedIndex(workingDayToModify.getStart().getRoundedDateTime().getHour());
        	
        	switch(workingDayToModify.getStart().getRoundedDateTime().getMinute()) {
            case 0:
            	startMinuteList.setSelectedIndex(0);
            	break;
            case 15:
            	startMinuteList.setSelectedIndex(1);
            	break;
            case 30:
            	startMinuteList.setSelectedIndex(2);
            	break;
            case 45:
            	startMinuteList.setSelectedIndex(3);
            	break;
        	default:
        		startMinuteList.setSelectedIndex(0);
        		break;
            }
        }
        
        if(workingDayToModify.getEnd() != null) {
        	endyearField.setText(Integer.toString(workingDayToModify.getEnd().getRoundedDateTime().getYear()));
            endmonthField.setText(Integer.toString(workingDayToModify.getEnd().getRoundedDateTime().getMonthValue()));
            enddayField.setText(Integer.toString(workingDayToModify.getEnd().getRoundedDateTime().getDayOfMonth()));
        	
        	endHourList.setSelectedIndex(workingDayToModify.getEnd().getRoundedDateTime().getHour());
        	 
        	switch(workingDayToModify.getEnd().getRoundedDateTime().getMinute()) {
            case 0:
                endMinuteList.setSelectedIndex(0);
            	break;
            case 15:
                endMinuteList.setSelectedIndex(1);
            	break;
            case 30:
                endMinuteList.setSelectedIndex(2);
            	break;
            case 45:
                endMinuteList.setSelectedIndex(3);
            	break;
        	default:
                endMinuteList.setSelectedIndex(0);
        		break;
            }
        }
        
		JButton createButton = new JButton("Save");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean error = false;
				boolean fullDay = false;
				try {
					Integer.parseInt(startyearField.getText());
					Integer.parseInt(startmonthField.getText());
					Integer.parseInt(startdayField.getText());
					//Soit c'était un cio complete et il faut forcement une fin
					if(typeCheckInOut != 3) {
						Integer.parseInt(endyearField.getText());
						Integer.parseInt(endmonthField.getText());
						Integer.parseInt(enddayField.getText());
						fullDay = true;
					}
					//Soit c'etait un cio non complete et on ne verifie que si il a une fin correcte (fin a null ok)
					else {
						if(endyearField.getText().equals("") && endmonthField.getText().equals("") && enddayField.getText().equals("")) {
							fullDay = false;
						}
						else {
							Integer.parseInt(endyearField.getText());
							Integer.parseInt(endmonthField.getText());
							Integer.parseInt(enddayField.getText());
							fullDay = true;
						}
					}
				}catch(java.lang.NumberFormatException e) {
					error = true;
					JOptionPane.showMessageDialog(parent, "Please enter integers only", "Unable to modify checkInOut", JOptionPane.ERROR_MESSAGE);
				}
				
				if(error == false) {
					
					CheckInOut startCIO = new CheckInOut();
					startCIO.setIdEmployee(((Employee)employeeList.getSelectedItem()).getIdentifiant());
					LocalDateTime startDate = LocalDateTime.now()
							.withYear(Integer.parseInt(startyearField.getText()))
							.withMonth(Integer.parseInt(startmonthField.getText()))
							.withDayOfMonth(Integer.parseInt(startdayField.getText()))
							.withHour((int)startHourList.getSelectedItem())
							.withMinute((int)startMinuteList.getSelectedItem());
					startCIO.setRoundedDateTime(startDate);
					
					CheckInOut endCIO = new CheckInOut();
					if(fullDay == true) {
						endCIO.setIdEmployee(((Employee)employeeList.getSelectedItem()).getIdentifiant());
						LocalDateTime endDate = LocalDateTime.now()
								.withYear(Integer.parseInt(endyearField.getText()))
								.withMonth(Integer.parseInt(endmonthField.getText()))
								.withDayOfMonth(Integer.parseInt(enddayField.getText()))
								.withHour((int)endHourList.getSelectedItem())
								.withMinute((int)endMinuteList.getSelectedItem());
						endCIO.setRoundedDateTime(endDate);
					}
	
					if( !endCIO.getRoundedDateTime().isBefore(startCIO.getRoundedDateTime()) ) {
						switch(typeCheckInOut) {
				        case 1:
				        	DataCentral.removeWorkingDayInEmployee(emp, workingDayToModify);
				        	emp.addWorkingDay(new WorkingDay(((Employee)employeeList.getSelectedItem()).getIdentifiant(), startCIO, endCIO));
				        	break;
				        case 2:
				        	DataCentral.removeWorkingDayInEmployee(emp, workingDayToModify);
				        	emp.addWorkingDay(new WorkingDay(((Employee)employeeList.getSelectedItem()).getIdentifiant(), startCIO, endCIO));
				        	break;
				        case 3:
				        	DataCentral.removeWorkingDayInData(emp);
				        	if(fullDay == true) {
				        		emp.addWorkingDay(new WorkingDay(((Employee)employeeList.getSelectedItem()).getIdentifiant(), startCIO, endCIO));
				        	}
				        	else {
				        		DataCentral.addCheckInOut(startCIO);
				        	}
				        	break;
					}
					
					WorkingDay.deleteWorkingDay(workingDayToModify);
					
					//On applique les changements :
					refreshTables();
					try {
						//	=	SAUVEGARDER DANS LE FICHIER			=
						ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
					} catch (IOException e) {
						e.printStackTrace();
					}
					dialogPanel.dispose();
					}
					else {
						JOptionPane.showMessageDialog(parent, "End date can't be before start date.", "Unable to modify checkInOut", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(employeeLabel)
    		.addComponent(startDateLabel)
    		.addComponent(startTimeLabel)
    		.addComponent(endDateLabel)
    		.addComponent(endTimeLabel)
        );
        hGroup.addGroup(bodyLayout.createParallelGroup()
            	.addComponent(employeeList)
        		.addComponent(startDatePanel)
            	.addComponent(startTimePanel)
        		.addComponent(endDatePanel)
        		.addComponent(endTimePanel)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(employeeLabel).addComponent(employeeList));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(startDateLabel).addComponent(startDatePanel));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(startTimeLabel).addComponent(startTimePanel));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(endDateLabel).addComponent(endDatePanel));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(endTimeLabel).addComponent(endTimePanel));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Edit checkInOut");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/edit.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenetre popup affichee lorsque l'on clique sur l'engrenage des options
	 * @param parent : La frame principale
	 */
	public static void dialogPanelSettings(JFrame parent) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
		
		JLabel labelTimeTracker = new JLabel("IP time tracker :");
		JFormattedTextField fieldIp = new JFormattedTextField();
		
		
		JLabel labelPortTC = new JLabel("Port TimeTracker to Central :");
		JFormattedTextField fieldPortTC = new JFormattedTextField();
		
		JLabel labelPortCT = new JLabel("Port Central to TimeTracker :");
		JFormattedTextField fieldPortCT = new JFormattedTextField();
		
		JLabel labelThreshold = new JLabel("Threshold : (min)");
		JFormattedTextField fieldThreshold = new JFormattedTextField();
		
		if(DataCentral.getParameters() == null) {
			fieldIp.setText("");
			fieldPortTC.setText("");
			fieldPortCT.setText("");
			fieldThreshold.setText("");
		}
		else {
			fieldIp.setText(DataCentral.getParameters().getIp());
			fieldPortTC.setText(Integer.toString(DataCentral.getParameters().getPortPtoC()));
			fieldPortCT.setText(Integer.toString(DataCentral.getParameters().getPortCtoP()));
			fieldThreshold.setText(Integer.toString(DataCentral.getParameters().getIncidentThreshold()));
		}
		
		JButton createButton = new JButton("Save");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean error = false;
				//On vérifie que l'ip n'est pas vide
				if(!isNullOrWhitespace(fieldIp.getText())){
					try{
						if(DataCentral.getParameters() == null) {
				    		DataCentral.setParameters(new Parameters());
				    	}
						DataCentral.getParameters().setIp(fieldIp.getText());
						DataCentral.getParameters().setPortPtoC(Integer.parseInt(fieldPortTC.getText()));
						DataCentral.getParameters().setPortCtoP(Integer.parseInt(fieldPortCT.getText()));
						DataCentral.getParameters().setIncidentThreshold(Integer.parseInt(fieldThreshold.getText()));
						error = false;
					}
					catch(java.lang.NullPointerException e) {
						error = true;
						JOptionPane.showMessageDialog(parent, "Please fill all fields before saving", "Unable to save parameters", JOptionPane.ERROR_MESSAGE);
					}
					catch(java.lang.NumberFormatException e) {
						error = true;
						JOptionPane.showMessageDialog(parent, "Please fill all fields before saving", "Unable to save parameters", JOptionPane.ERROR_MESSAGE);
					}
					if(error == false) {
						//On applique les changements :
						refreshTables();
						try {
							//	=	SAUVEGARDER DANS LE FICHIER			=
							ReadWriteCentral.saveParametersCentral(DataCentral.adressSaveParametersCentral);
							//	=	ENVOYER A LA POINTEUSE				=
							new Thread(new CentralClient()).start();
						} catch (IOException e) {
							e.printStackTrace();
						}
						dialogPanel.dispose();
					}
				}
				else {
					JOptionPane.showMessageDialog(parent, "Please fill all fields before saving", "Unable to save parameters", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(labelTimeTracker)
    		.addComponent(labelPortTC)
    		.addComponent(labelPortCT)
    		.addComponent(labelThreshold)
        );
        hGroup.addGroup(bodyLayout.createParallelGroup()
            	.addComponent(fieldIp)
            	.addComponent(fieldPortTC)
        		.addComponent(fieldPortCT)
        		.addComponent(fieldThreshold)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(labelTimeTracker).addComponent(fieldIp));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(labelPortTC).addComponent(fieldPortTC));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(labelPortCT).addComponent(fieldPortCT));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(labelThreshold).addComponent(fieldThreshold));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.setMinimumSize(new Dimension(300, 100));
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Settings");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/settings.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenetre popup affichee lorsque l'on clique sur "Create department"
	 * @param parent : La frame principale
	 */
	public static void dialogPanelCreateDepartment(JFrame parent) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
        
        JLabel departmentLabel = new JLabel("Name");
		JTextField nameField = new JTextField(10);
		
		JLabel managerLabel = new JLabel("Manager");
		JComboBox<Employee> employeeList = new JComboBox<Employee>();
		if(DataCentral.getEmployees() != null) {
			for(Employee e : DataCentral.getEmployees()){
				employeeList.addItem(e);
			}
		}
		if(DataCentral.getManagers() != null) {
			for(Manager m : DataCentral.getManagers()){
				employeeList.addItem((Employee) m);
			}
		}
        
		JButton createButton = new JButton("Create");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				boolean nameAlreadyExists = false;
				if(DataCentral.getDepartments() != null) {
					for(Department d : DataCentral.getDepartments()){
						if(d.getName().equals(nameField.getText())) {
							nameAlreadyExists = true;
						}
					}
				}
				
				if(nameAlreadyExists == true) {
					JOptionPane.showMessageDialog(parent, "A department with the same name already exists", "Unable to create department", JOptionPane.ERROR_MESSAGE);
				}
				else {
					
					boolean isManager = false;
					if(DataCentral.getManagers() != null) {
						for(Manager m : DataCentral.getManagers()){
							if(((Employee) employeeList.getSelectedItem()).getIdentifiant() == m.getIdentifiant()) {
								isManager = true;
							}
						}
					}
					
					//On crée le nouveau département 
					Department newDepartment = new Department(nameField.getText());
					DataCentral.addDepartment(newDepartment);
					
					//Si c'était un manager, on le change de département
					if(isManager == true) {
						for(Department d : DataCentral.getDepartments()) {
							if(d.getManager() == ((Employee)employeeList.getSelectedItem()).getIdentifiant()) {
								d.setManager(-1);
							}
						}
						newDepartment.setManager(((Manager)employeeList.getSelectedItem()).getIdentifiant());
						((Manager)employeeList.getSelectedItem()).setIdDepartment(newDepartment.getId());
					}
					//Sinon on change l'employé en manager
					else {
						if(employeeList.getSelectedItem() != null) {
							Manager newManager = new Manager((Employee)employeeList.getSelectedItem());
							DataCentral.delEmployee((Employee)employeeList.getSelectedItem());
							DataCentral.addManager(newManager);
							newDepartment.setManager(newManager.getIdentifiant());
							newManager.setIdDepartment(newDepartment.getId());
						}
						else {
							//newDepartment.setManager(-1);
						}
					}
					
					//On applique les changements :
					refreshTables();
					try {
						//	=	SAUVEGARDER DANS LE FICHIER			=
						ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
						//	=	ENVOYER A LA POINTEUSE				=
						new Thread(new CentralClient()).start();
					} catch (IOException e) {
						e.printStackTrace();
					}
					dialogPanel.dispose();
				}
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(departmentLabel)
    		.addComponent(managerLabel)
        );
        hGroup.addGroup(bodyLayout.createParallelGroup()
            	.addComponent(nameField)
            	.addComponent(employeeList)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(departmentLabel).addComponent(nameField));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(managerLabel).addComponent(employeeList));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Create a new department");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/new.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenetre popup affichee lorsque l'on clique sur "Modify department"
	 * @param parent : La frame principale
	 * @param department : Le département à modifier
	 */
	public static void dialogPanelModifyDepartment(JFrame parent, Department department) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
        
        JLabel departmentLabel = new JLabel("Department");
        JComboBox<Department> departmentList = new JComboBox<Department>();
		for(Department d : DataCentral.getDepartments()){
			departmentList.addItem(d);
		}
		departmentList.setSelectedItem(department);
		
		JLabel managerLabel = new JLabel("Manager");
		JComboBox<Employee> employeeList = new JComboBox<Employee>();
		for(Employee e : DataCentral.getEmployees()){
			employeeList.addItem(e);
		}
		for(Manager m : DataCentral.getManagers()){
			employeeList.addItem((Employee) m);
		}
		
		JLabel nameLabel = new JLabel("New name");
		JTextField nameField = new JTextField(10);
		departmentList.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	//Selectionner le manager précédent dans la liste et le texte correspondant au nom du département
				int iIndice = DataCentral.getEmployees().size();
				int iIndiceManager = -1;
				for(Manager m : DataCentral.getManagers()) {
					if(m.getIdentifiant() == ((Department)departmentList.getSelectedItem()).getManager()) {
						iIndiceManager = iIndice;
					}
					iIndice ++;
				}
				employeeList.setSelectedIndex(iIndiceManager);
		    	nameField.setText(((Department)departmentList.getSelectedItem()).getName());
		    }
		});
		
		//Sélectionner les informations correspondant au département au moins une fois
		//Selectionner le manager précédent dans la liste et le texte correspondant au nom au nom du département
		int iIndice = DataCentral.getEmployees().size();
		int iIndiceManager = -1;
		for(Manager m : DataCentral.getManagers()) {
			if(m.getIdentifiant() == ((Department)departmentList.getSelectedItem()).getManager()) {
				iIndiceManager = iIndice;
			}
			iIndice ++;
		}
		employeeList.setSelectedIndex(iIndiceManager);
		nameField.setText(((Department)departmentList.getSelectedItem()).getName());
        
		JButton createButton = new JButton("Modify");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(nameField.getText()==null || nameField.getText().equals("") || nameField.getText().equals(" ")  || nameField.getText().isEmpty()==true) {
					JOptionPane.showMessageDialog(parent, "Please enter a name for the department.", "Unable to modify department", JOptionPane.ERROR_MESSAGE);
				}
				else {
					
					if(employeeList.getSelectedItem() != null) {
						
						boolean isManager = false;
						for(Manager m : DataCentral.getManagers()){
							if(((Employee) employeeList.getSelectedItem()).getIdentifiant() == m.getIdentifiant()) {
								isManager = true;
							}
						}
						
						//Si c'était un manager, on le change de département
						if(isManager == true) {
							for(Department d : DataCentral.getDepartments()) {
								if(d.getManager() == ((Employee)employeeList.getSelectedItem()).getIdentifiant()) {
									d.setManager(-1);
								}
							}
							((Department)departmentList.getSelectedItem()).setManager(((Manager)employeeList.getSelectedItem()).getIdentifiant());
							((Manager)employeeList.getSelectedItem()).setIdDepartment(((Department)departmentList.getSelectedItem()).getId());
						}
						//Sinon on change l'employé en manager
						else {
							Manager newManager = new Manager((Employee)employeeList.getSelectedItem());
							DataCentral.delEmployee((Employee)employeeList.getSelectedItem());
							DataCentral.addManager(newManager);
							((Department)departmentList.getSelectedItem()).setManager(newManager.getIdentifiant());
							newManager.setIdDepartment(((Department)departmentList.getSelectedItem()).getId());
						}
						
						//Changer le nom du département
						((Department)departmentList.getSelectedItem()).setName(nameField.getText());
						
						//On applique les changements :
						refreshTables();
						try {
							//	=	SAUVEGARDER DANS LE FICHIER			=
							ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
							//	=	ENVOYER A LA POINTEUSE				=
							new Thread(new CentralClient()).start();
						} catch (IOException e) {
							e.printStackTrace();
						}
						dialogPanel.dispose();
					}
					else {
						JOptionPane.showMessageDialog(parent, "Please select a manager.", "Unable to modify department", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(departmentLabel)
    		.addComponent(nameLabel)
    		.addComponent(managerLabel)
        );
        hGroup.addGroup(bodyLayout.createParallelGroup()
            	.addComponent(departmentList)
        		.addComponent(nameField)
            	.addComponent(employeeList)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(departmentLabel).addComponent(departmentList));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(managerLabel).addComponent(employeeList));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Modify department");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/edit.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenetre popup affichee lorsque l'on clique sur "Delete department"
	 * @param parent : La frame principale
	 */
	public static void dialogPanelDeleteDepartment(JFrame parent) {

		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
        
        JComboBox<Department> departmentList = new JComboBox<Department>();
		for(Department d : DataCentral.getDepartments()){
			departmentList.addItem(d);
		}
		
		JButton createButton = new JButton("Delete");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DataCentral.delDepartment((Department)departmentList.getSelectedItem());
				
				//On applique les changements :
				refreshTables();
				try {
					//	=	SAUVEGARDER DANS LE FICHIER			=
					ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
					//	=	ENVOYER A LA POINTEUSE				=
					new Thread(new CentralClient()).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				dialogPanel.dispose();
				
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(departmentList)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(departmentList));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Delete a department");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/delete.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenetre popup affichee lorsque l'on clique sur "Create CheckInOut"
	 * @param parent : La frame principale
	 */
	public static void dialogPanelCreateCheckInOut(JFrame parent) {
		
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
        
        JLabel employeeLabel = new JLabel("Employee");
        JComboBox<Employee> employeeList = new JComboBox<Employee>();
        
        JLabel dateLabel = new JLabel("Check date");
		JTextField yearField = new JTextField(2);
		JTextField monthField = new JTextField(1);
		JTextField dayField = new JTextField(1);
		
        JPanel datePanel = new JPanel();
        GroupLayout dateLayout = new GroupLayout(datePanel);
        datePanel.setLayout(dateLayout);
        
        GroupLayout.SequentialGroup hDateGroup = dateLayout.createSequentialGroup();
        hDateGroup.addGroup(dateLayout.createParallelGroup().addComponent(yearField));
        hDateGroup.addGroup(dateLayout.createParallelGroup().addComponent(monthField));
        hDateGroup.addGroup(dateLayout.createParallelGroup().addComponent(dayField));
        dateLayout.setHorizontalGroup(hDateGroup);
        
        GroupLayout.SequentialGroup vDateGroup = dateLayout.createSequentialGroup();
        vDateGroup.addGroup(dateLayout.createParallelGroup(Alignment.CENTER).addComponent(yearField).addComponent(monthField).addComponent(dayField));
        dateLayout.setVerticalGroup(vDateGroup);
        
        JLabel timeLabel = new JLabel("Check time");
		JComboBox<Integer> hourList = new JComboBox<Integer>();
		JComboBox<Integer> minuteList = new JComboBox<Integer>();
        JPanel hourPanel = new JPanel();
        hourPanel.add(hourList);
        hourPanel.add(new JLabel("h   "));
        hourPanel.add(minuteList);
        hourPanel.add(new JLabel("min"));

		//Remplir les listes :
        for(Employee e : DataCentral.getEmployees()){
        	employeeList.addItem(e);
        }
        for(Manager m : DataCentral.getManagers()) {
			employeeList.addItem((Employee) m);
		}
        
        for(int iIndice=0; iIndice<24; iIndice++){
        	hourList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<=45; iIndice+=15){
        	minuteList.addItem(iIndice);
        }
        
        //Selectionner l'heure et la date locale :
		yearField.setText(LocalDateTime.now().getYear() + "");
		monthField.setText(LocalDateTime.now().getMonthValue() + "");
		dayField.setText(LocalDateTime.now().getDayOfMonth() + "");
        hourList.setSelectedIndex((DateTimeTools.roundHour(LocalDateTime.now())).getHour());
        int localMinute = (DateTimeTools.roundHour(LocalDateTime.now())).getMinute();
        int listMinuteIndex = 0;
        switch(localMinute) {
        case 0:
        	listMinuteIndex = 0;
        	break;
        case 15:
        	listMinuteIndex = 1;
        	break;
        case 30:
        	listMinuteIndex = 2;
        	break;
        case 45:
        	listMinuteIndex = 3;
        	break;
        }
        minuteList.setSelectedIndex(listMinuteIndex);
		
		JButton createButton = new JButton("Create");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean error = false;
				try {
					Integer.parseInt(yearField.getText());
					Integer.parseInt(monthField.getText());
					Integer.parseInt(dayField.getText());
				}catch(java.lang.NumberFormatException e) {
					error = true;
					JOptionPane.showMessageDialog(parent, "Please enter integers only", "Unable to create checkInOut", JOptionPane.ERROR_MESSAGE);
				}
				if(error == false) {
					try {
						LocalDateTime d = LocalDateTime.now()
								.withYear(Integer.parseInt(yearField.getText()))
								.withMonth(Integer.parseInt(monthField.getText()))
								.withDayOfMonth(Integer.parseInt(dayField.getText()))
								.withHour((int) hourList.getSelectedItem())
								.withMinute((int) minuteList.getSelectedItem());
						
						CheckInOut cio = new CheckInOut(d);
						cio.setIdEmployee(((Employee) employeeList.getSelectedItem()).getIdentifiant()); 
						
						DataCentral.addCheckInOut(cio);

						refreshTables();
						try {
							//	=	SAUVEGARDER DANS LE FICHIER			=
							ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
						} catch (IOException e) {
							e.printStackTrace();
						}
						dialogPanel.dispose();
					}
					catch(java.time.DateTimeException e) {
						JOptionPane.showMessageDialog(parent, "Please enter a valid date", "Unable to create checkInOut", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});
		
		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);

		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(employeeLabel)
    		.addComponent(dateLabel)
    		.addComponent(timeLabel)
        );
        hGroup.addGroup(bodyLayout.createParallelGroup()
        	.addComponent(employeeList)
    		.addComponent(datePanel)
        	.addComponent(hourPanel)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.CENTER).addComponent(employeeLabel).addComponent(employeeList));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.CENTER).addComponent(dateLabel).addComponent(datePanel));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.CENTER).addComponent(timeLabel).addComponent(hourPanel));
        bodyLayout.setVerticalGroup(vGroup);
        
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Create a new check in/out");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/new.png"));
        dialogPanel.setIconImage(img.getImage());
	}
		
	/**
	 * Fenetre popup affichee lorsque l'on clique sur "Create employee"
	 * @param parent : La frame principale
	 */
	@SuppressWarnings("unchecked")
	public static void dialogPanelCreateEmployee(JFrame parent) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
        
        JLabel nameLabel = new JLabel("Name");
		JTextField nameField = new JTextField(10);
        JLabel surnameLabel = new JLabel("Surname");
		JTextField surnameField = new JTextField(10);
        JLabel emailLabel = new JLabel("@mail");
		JTextField emailField = new JTextField(10);

        JLabel departmentLabel = new JLabel("Department");
		JComboBox<Department> departmentsList = new JComboBox<Department>();
		for(Department d : DataCentral.getDepartments()){
			departmentsList.addItem(d);
		}
		
        JLabel startLabel = new JLabel("Start hour");
		JComboBox<Integer> startHourList = new JComboBox<Integer>();
		JComboBox<Integer> startMinuteList = new JComboBox<Integer>();
        JPanel startHourPanel = new JPanel();
        startHourPanel.add(startHourList);
        startHourPanel.add(new JLabel("h   "));
        startHourPanel.add(startMinuteList);
        startHourPanel.add(new JLabel("min"));
        
        JLabel endLabel = new JLabel("End hour");
		JComboBox<Integer> endHourList = new JComboBox<Integer>();
		JComboBox<Integer> endMinuteList = new JComboBox<Integer>();
        JPanel endHourPanel = new JPanel();
        endHourPanel.add(endHourList);
        endHourPanel.add(new JLabel("h   "));
        endHourPanel.add(endMinuteList);
        endHourPanel.add(new JLabel("min"));

		//Remplir les listes :
        for(int iIndice=0; iIndice<24; iIndice++){
        	startHourList.addItem(iIndice);
        	endHourList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<=45; iIndice+=15){
        	startMinuteList.addItem(iIndice);
        	endMinuteList.addItem(iIndice);
        }
        
        //Selectionner les heures de début et de fin de base :
        startHourList.setSelectedIndex(8);
        endHourList.setSelectedIndex(17);
        startMinuteList.setSelectedIndex(0);
        endMinuteList.setSelectedIndex(0);
		
        JLabel managerLabel = new JLabel("Manager");
        JPanel managerPanel = new JPanel();
		@SuppressWarnings("rawtypes")
		JComboBox departmentsToManageList = new JComboBox();
		departmentsToManageList.addItem("Not managing");
		for(Department d : DataCentral.getDepartments()){
			departmentsToManageList.addItem(d);
		}
		JCheckBox isManagerBox = new JCheckBox("No", false);
		isManagerBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
				if(abstractButton.getModel().isSelected() == true) {
					isManagerBox.setText("Yes");
					departmentsToManageList.setVisible(true);
					dialogPanel.pack();
				}
				else {
					isManagerBox.setText("No");
					departmentsToManageList.setVisible(false);
					dialogPanel.pack();
				}
			}
		});
		managerPanel.add(isManagerBox);
		managerPanel.add(departmentsToManageList);
		managerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));	
		departmentsToManageList.setVisible(false);
		
		JButton createButton = new JButton("Create");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(nameField.getText().equals("") || surnameField.getText().equals("")) {
					JOptionPane.showMessageDialog(parent, "Please specify at least the name and the surname", "Unable to create employee", JOptionPane.ERROR_MESSAGE);
				}
				else {
					if(isManagerBox.isSelected()==true && departmentsToManageList.getSelectedItem().getClass().getSimpleName().equals("Department") && ((Department)departmentsToManageList.getSelectedItem()).getId() != ((Department)departmentsList.getSelectedItem()).getId()) {
						JOptionPane.showMessageDialog(parent, "A manager can't be in a different department from the one he manages.", "Unable to create manager", JOptionPane.ERROR_MESSAGE);
					}
					else {
						//Si on a coché la boite "isManager"
						if(departmentsToManageList.isVisible() == true) {
							Manager newManager = new Manager();
							newManager.setName(nameField.getText());
							newManager.setSurname(surnameField.getText());
							newManager.setMail(emailField.getText());
							newManager.setIdDepartment(((Department) departmentsList.getSelectedItem()).getId());
							newManager.setEndHour(LocalTime.now().withHour((int)endHourList.getSelectedItem()).withMinute((int)endMinuteList.getSelectedItem()));
							newManager.setStartHour(LocalTime.now().withHour((int)startHourList.getSelectedItem()).withMinute((int)startMinuteList.getSelectedItem()));
							
							DataCentral.addManager(newManager);
							
							//Si on a selectionné un departement à manager :
							if(departmentsToManageList.getSelectedItem().getClass().getSimpleName().equals("Department")) {
								int iIndice=0;
								for(Department d : DataCentral.getDepartments()){
									if(d.getId() == ((Department)departmentsToManageList.getSelectedItem()).getId()) {
										DataCentral.getDepartments().get(iIndice).setManager(newManager.getIdentifiant());
									}
									iIndice++;
								}
							}
						}
						//Sinon c'est un employé
						else {
							Employee newEmployee = new Employee();
							newEmployee.setName(nameField.getText());
							newEmployee.setSurname(surnameField.getText());
							newEmployee.setMail(emailField.getText());
							newEmployee.setIdDepartment(((Department) departmentsList.getSelectedItem()).getId());
							newEmployee.setEndHour(LocalTime.now().withHour((int)endHourList.getSelectedItem()).withMinute((int)endMinuteList.getSelectedItem()));
							newEmployee.setStartHour(LocalTime.now().withHour((int)startHourList.getSelectedItem()).withMinute((int)startMinuteList.getSelectedItem()));
							DataCentral.addEmployee(newEmployee);
						}
						
						//On applique les changements :
						refreshTables();
						try {
							//	=	SAUVEGARDER DANS LE FICHIER			=
							ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
							//	=	ENVOYER A LA POINTEUSE				=
							new Thread(new CentralClient()).start();
						} catch (IOException e) {
							e.printStackTrace();
						}
						dialogPanel.dispose();
					}
					
				}
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(nameLabel)
    		.addComponent(surnameLabel)
    		.addComponent(emailLabel)
    		.addComponent(departmentLabel)
    		.addComponent(startLabel)
    		.addComponent(endLabel)
    		.addComponent(managerLabel)
        );
        hGroup.addGroup(bodyLayout.createParallelGroup()
        	.addComponent(nameField)
        	.addComponent(surnameField)
        	.addComponent(emailField)
        	.addComponent(departmentsList)
        	.addComponent(startHourPanel)
        	.addComponent(endHourPanel)
        	.addComponent(managerPanel)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(surnameLabel).addComponent(surnameField));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(emailLabel).addComponent(emailField));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(departmentLabel).addComponent(departmentsList));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(startLabel).addComponent(startHourPanel));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(endLabel).addComponent(endHourPanel));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(managerLabel).addComponent(managerPanel));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Create a new employee");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/new.png"));
        dialogPanel.setIconImage(img.getImage());
	}

	/**
	 * Fenetre popup affichee lorsque l'on clique sur "Delete employee"
	 * @param parent : La frame principale
	 */
	public static void dialogPanelDeleteEmployee(JFrame parent) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
        
        JComboBox<Employee> employeeList = new JComboBox<Employee>();
		for(Employee e : DataCentral.getEmployees()){
			employeeList.addItem(e);
		}
		for(Manager m : DataCentral.getManagers()) {
			employeeList.addItem((Employee) m);
		}
		
		JButton createButton = new JButton("Delete");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Check if the employee is actually a manager :
				boolean isManager = false;
				for(Manager m : DataCentral.getManagers()) {
					if(m.getIdentifiant()==((Employee)employeeList.getSelectedItem()).getIdentifiant()) {
						isManager = true;
					}
				}
				if(isManager == true) {
					System.out.println("deleting manager");
					DataCentral.delManager((Manager)employeeList.getSelectedItem());
				}
				else {
					DataCentral.delEmployee((Employee)employeeList.getSelectedItem());
				}
				
				//On applique les changements :
				refreshTables();
				try {
					//	=	SAUVEGARDER DANS LE FICHIER			=
					ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
					//	=	ENVOYER A LA POINTEUSE				=
					new Thread(new CentralClient()).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				dialogPanel.dispose();
				
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(employeeList)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(employeeList));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Delete an employee");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/delete.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenetre servant uniquement à choisir l'employé à modifier
	 * @param parent : La frame principale
	 */
	public static void dialogPanelSelectEmployee(JFrame parent) {
		
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
        
        JComboBox<Employee> employeeList = new JComboBox<Employee>();
		for(Employee e : DataCentral.getEmployees()){
			employeeList.addItem(e);
		}
		for(Manager m : DataCentral.getManagers()) {
			employeeList.addItem((Employee) m);
		}
		
		JButton createButton = new JButton("Modify");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanelModifyEmployee(parent, (Employee) employeeList.getSelectedItem());
				dialogPanel.dispose();
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(employeeList)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(employeeList));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Select the employee to modify");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/edit.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fenêtre appelée par dialogPanelSelectEmployee qui permet de modifier l'employé passé en paramètre
	 * @param parent : La frame principale
	 * @param employeeToModify : L'employé à modifier
	 */
	@SuppressWarnings("unchecked")
	public static void dialogPanelModifyEmployee(JFrame parent, Employee employeeToModify) {
		JDialog dialogPanel = new JDialog();
		
		JPanel mainPanel = new JPanel();
		dialogPanel.getContentPane().add(mainPanel);
        GroupLayout fullLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(fullLayout);
        
		JPanel bodyPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
        GroupLayout.SequentialGroup hMainGroup = fullLayout.createSequentialGroup();
        hMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel).addComponent(buttonsPanel));
        fullLayout.setHorizontalGroup(hMainGroup);
        
        GroupLayout.SequentialGroup vMainGroup = fullLayout.createSequentialGroup();
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(bodyPanel));
        vMainGroup.addGroup(fullLayout.createParallelGroup(Alignment.CENTER).addComponent(buttonsPanel));
        fullLayout.setVerticalGroup(vMainGroup);
		

        GroupLayout bodyLayout = new GroupLayout(bodyPanel);
        bodyLayout.setAutoCreateGaps(true);
        bodyLayout.setAutoCreateContainerGaps(true);
        
        GroupLayout buttonsLayout = new GroupLayout(buttonsPanel);
        buttonsLayout.setAutoCreateGaps(true);
        buttonsLayout.setAutoCreateContainerGaps(true);
        
        bodyPanel.setLayout(bodyLayout);
        buttonsPanel.setLayout(buttonsLayout);
        
        JLabel nameLabel = new JLabel("Name");
		JTextField nameField = new JTextField(10);
		nameField.setText(employeeToModify.getName());
        JLabel surnameLabel = new JLabel("Surname");
		JTextField surnameField = new JTextField(10);
		surnameField.setText(employeeToModify.getSurname());
        JLabel emailLabel = new JLabel("@mail");
		JTextField emailField = new JTextField(10);
		emailField.setText(employeeToModify.getMail());

        JLabel departmentLabel = new JLabel("Department");
		JComboBox<Department> departmentsList = new JComboBox<Department>();
		for(Department d : DataCentral.getDepartments()){
			departmentsList.addItem(d);
		}
		for(Department d : DataCentral.getDepartments()) {
			if(d.getId() == employeeToModify.getIdDepartment()) {
				departmentsList.setSelectedItem(d);
			}
		}
		
        JLabel startLabel = new JLabel("Start hour");
		JComboBox<Integer> startHourList = new JComboBox<Integer>();
		JComboBox<Integer> startMinuteList = new JComboBox<Integer>();
        JPanel startHourPanel = new JPanel();
        startHourPanel.add(startHourList);
        startHourPanel.add(new JLabel("h   "));
        startHourPanel.add(startMinuteList);
        startHourPanel.add(new JLabel("min"));
        
        JLabel endLabel = new JLabel("End hour");
		JComboBox<Integer> endHourList = new JComboBox<Integer>();
		JComboBox<Integer> endMinuteList = new JComboBox<Integer>();
        JPanel endHourPanel = new JPanel();
        endHourPanel.add(endHourList);
        endHourPanel.add(new JLabel("h   "));
        endHourPanel.add(endMinuteList);
        endHourPanel.add(new JLabel("min"));
        
		//Remplir les listes :
        for(int iIndice=0; iIndice<24; iIndice++){
        	startHourList.addItem(iIndice);
        	endHourList.addItem(iIndice);
        }
        for(int iIndice=0; iIndice<=45; iIndice+=15){
        	startMinuteList.addItem(iIndice);
        	endMinuteList.addItem(iIndice);
        }
        
        //Selectionner les heures de début et de fin de base :
        startHourList.setSelectedIndex(employeeToModify.getStartHour().getHour());
        endHourList.setSelectedIndex(employeeToModify.getEndHour().getHour());
        startMinuteList.setSelectedIndex(employeeToModify.getStartHour().getMinute());
        endMinuteList.setSelectedIndex(employeeToModify.getEndHour().getMinute());
		
        JLabel managerLabel = new JLabel("Manager");
        JPanel managerPanel = new JPanel();
		@SuppressWarnings("rawtypes")
		JComboBox departmentsToManageList = new JComboBox();
		departmentsToManageList.addItem("Not managing");
		for(Department d : DataCentral.getDepartments()){
			departmentsToManageList.addItem(d);
		}
		JCheckBox isManagerBox = new JCheckBox("No", false);
		isManagerBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
				if(abstractButton.getModel().isSelected() == true) {
					isManagerBox.setText("Yes");
					departmentsToManageList.setVisible(true);
				}
				else {
					isManagerBox.setText("No");
					departmentsToManageList.setVisible(false);
				}
			}
		});
		managerPanel.add(isManagerBox);
		managerPanel.add(departmentsToManageList);
		managerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));	
		//Check if the employee is actually a manager :
		boolean isManager = false;
		for(Manager m : DataCentral.getManagers()) {
			if(m.getIdentifiant()==employeeToModify.getIdentifiant()) {
				isManager = true;
			}
		}
		departmentsToManageList.setVisible(isManager);
		isManagerBox.setSelected(isManager);
		if(isManager == true) {
			isManagerBox.setText("Yes");
		}
		//Select the correct department :
		for(Department d : DataCentral.getDepartments()) {
			if(d.getManager() == employeeToModify.getIdentifiant()) {
				departmentsToManageList.setSelectedItem(d);
			}
		}

		JButton createButton = new JButton("Modify");
		createButton.setOpaque(true);
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Check if the employee is actually a manager :
				boolean wasManager = false;
				for(Manager m : DataCentral.getManagers()) {
					if(m.getIdentifiant()==employeeToModify.getIdentifiant()) {
						wasManager = true;
					}
				}
				
				if(nameField.getText().equals("") || surnameField.getText().equals("") || departmentsList.getSelectedItem()==null) {
					JOptionPane.showMessageDialog(parent, "Please specify at least the name, surname and department.", "Unable to create employee", JOptionPane.ERROR_MESSAGE);
				}
				else {
					if(isManagerBox.isSelected()==true && departmentsToManageList.getSelectedItem().getClass().getSimpleName().equals("Department") && ((Department)departmentsToManageList.getSelectedItem()).getId() != ((Department)departmentsList.getSelectedItem()).getId()) {
						JOptionPane.showMessageDialog(parent, "A manager can't be in a different department from the one he manages.", "Unable to create manager", JOptionPane.ERROR_MESSAGE);
					}
					else {
						
						if(wasManager == true) {
							//Un manager qui reste un manager
							if(isManagerBox.isSelected() == true) {
								//On l'enleve du poste de manager du departement precedent
								for(Department d : DataCentral.getDepartments()) {
									if(d.getManager() == employeeToModify.getIdentifiant()) {
										d.setManager(-1);
									}
								}
								
								DataCentral.getManagers().get(DataCentral.getManagers().indexOf(employeeToModify)).setEndHour(LocalTime.now().withHour((int)endHourList.getSelectedItem()).withMinute((int)endMinuteList.getSelectedItem()));
								DataCentral.getManagers().get(DataCentral.getManagers().indexOf(employeeToModify)).setStartHour(LocalTime.now().withHour((int)startHourList.getSelectedItem()).withMinute((int)startMinuteList.getSelectedItem()));
								DataCentral.getManagers().get(DataCentral.getManagers().indexOf(employeeToModify)).setMail(emailField.getText());
								DataCentral.getManagers().get(DataCentral.getManagers().indexOf(employeeToModify)).setSurname(surnameField.getText());
								DataCentral.getManagers().get(DataCentral.getManagers().indexOf(employeeToModify)).setName(nameField.getText());
								DataCentral.getManagers().get(DataCentral.getManagers().indexOf(employeeToModify)).setIdDepartment(((Department) departmentsList.getSelectedItem()).getId());
								
								//On le fait manager du département si il le faut
								if(departmentsToManageList.getSelectedItem().getClass().getSimpleName().equals("Department")) {
									for(Department d : DataCentral.getDepartments()){
										if(d.getId() == ((Department)departmentsToManageList.getSelectedItem()).getId()) {
											d.setManager(employeeToModify.getIdentifiant());
										}
									}
								}
							}
							//Un manager qui devient un employé
							else {
								for(Department d : DataCentral.getDepartments()){
									if(d.getManager() == employeeToModify.getIdentifiant()) {
										d.setManager(-1);
									}
								}
								
								Employee newEmployee = new Employee(employeeToModify);
								
								//On sauvegarde ses pointages
								CheckInOut saveCIO = null;
								int index = 0, indexCIO = -1;
								for(WorkingDay wd : DataCentral.getWorkingDays()) {
									if(wd.getIdEmployee() == employeeToModify.getIdentifiant()) {
										saveCIO = new CheckInOut(wd.getStart());
										indexCIO = index;
									}
									index++;
								}
								if(indexCIO != -1) {
									//DataCentral.getWorkingDays().remove(indexCIO);
								}
								DataCentral.delManager((Manager)employeeToModify);
								DataCentral.addEmployee(newEmployee);
								if(saveCIO != null) {
									saveCIO.setIdEmployee(newEmployee.getIdentifiant());
									DataCentral.addCheckInOut(saveCIO);
								}
							}
						}
						else {
							//Un employé qui reste un employé
							if(isManagerBox.isSelected() == false) {
								DataCentral.getEmployees().get(DataCentral.getEmployees().indexOf(employeeToModify)).setEndHour(LocalTime.now().withHour((int)endHourList.getSelectedItem()).withMinute((int)endMinuteList.getSelectedItem()));
								DataCentral.getEmployees().get(DataCentral.getEmployees().indexOf(employeeToModify)).setStartHour(LocalTime.now().withHour((int)startHourList.getSelectedItem()).withMinute((int)startMinuteList.getSelectedItem()));
								DataCentral.getEmployees().get(DataCentral.getEmployees().indexOf(employeeToModify)).setMail(emailField.getText());
								DataCentral.getEmployees().get(DataCentral.getEmployees().indexOf(employeeToModify)).setSurname(surnameField.getText());
								DataCentral.getEmployees().get(DataCentral.getEmployees().indexOf(employeeToModify)).setName(nameField.getText());
								DataCentral.getEmployees().get(DataCentral.getEmployees().indexOf(employeeToModify)).setIdDepartment(((Department) departmentsList.getSelectedItem()).getId());
							}
							//Un employé qui devient un manager
							else {
								Manager newManager = new Manager(employeeToModify);
								DataCentral.delEmployee(employeeToModify);
								DataCentral.addManager(newManager);
								//On le fait manager du département si il le faut
								if(departmentsToManageList.getSelectedItem().getClass().getSimpleName().equals("Department")) {
									int iIndice=0;
									for(Department d : DataCentral.getDepartments()){
										if(d.getId() == ((Department)departmentsToManageList.getSelectedItem()).getId()) {
											DataCentral.getDepartments().get(iIndice).setManager(newManager.getIdentifiant());
										}
										iIndice++;
									}
								}
							}
						}
						
						//On applique les changements :
						refreshTables();
						try {
							//	=	SAUVEGARDER DANS LE FICHIER			=
							ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
							//	=	ENVOYER A LA POINTEUSE				=
							new Thread(new CentralClient()).start();
						} catch (IOException e) {
							e.printStackTrace();
						}
						dialogPanel.dispose();
					}
				}
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setOpaque(true);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogPanel.dispose();
			}
		});

		GroupLayout.SequentialGroup hButtonGroup = buttonsLayout.createSequentialGroup();
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton));
		hButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(cancelButton));
		buttonsLayout.setHorizontalGroup(hButtonGroup);
        
        GroupLayout.SequentialGroup vButtonGroup = buttonsLayout.createSequentialGroup();
        vButtonGroup.addGroup(buttonsLayout.createParallelGroup(Alignment.CENTER).addComponent(createButton).addComponent(cancelButton));
        buttonsLayout.setVerticalGroup(vButtonGroup);
		
		GroupLayout.SequentialGroup hGroup = bodyLayout.createSequentialGroup();
        hGroup.addGroup(bodyLayout.createParallelGroup()
    		.addComponent(nameLabel)
    		.addComponent(surnameLabel)
    		.addComponent(emailLabel)
    		.addComponent(departmentLabel)
    		.addComponent(startLabel)
    		.addComponent(endLabel)
    		.addComponent(managerLabel)
        );
        hGroup.addGroup(bodyLayout.createParallelGroup()
        	.addComponent(nameField)
        	.addComponent(surnameField)
        	.addComponent(emailField)
        	.addComponent(departmentsList)
        	.addComponent(startHourPanel)
        	.addComponent(endHourPanel)
        	.addComponent(managerPanel)
        );
        bodyLayout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = bodyLayout.createSequentialGroup();
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(surnameLabel).addComponent(surnameField));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(emailLabel).addComponent(emailField));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(departmentLabel).addComponent(departmentsList));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(startLabel).addComponent(startHourPanel));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(endLabel).addComponent(endHourPanel));
        vGroup.addGroup(bodyLayout.createParallelGroup(Alignment.BASELINE).addComponent(managerLabel).addComponent(managerPanel));
        bodyLayout.setVerticalGroup(vGroup);
		
        dialogPanel.setVisible(true);
        dialogPanel.pack();
        dialogPanel.setLocationRelativeTo(null);
        //dialogPanel.setResizable(false);
        dialogPanel.setTitle("Modifying " + employeeToModify.getName() + " " + employeeToModify.getSurname() + "...");
        ImageIcon img = new ImageIcon(CentralInterface.class.getClassLoader().getResource("ressources/edit.png"));
        dialogPanel.setIconImage(img.getImage());
	}
	
	/**
	 * Fonction qui permet de vérifier est vide ou constitué uniquement d'espaces
	 * @param chaine
	 * @return false si ce n'est pas le cas, true sinon
	 */
	public static boolean isNullOrWhitespace(String chaine) {
	    if (chaine == null) {
	        return true;
	    }

	    for (int i=0; i < chaine.length(); i++) {
	        if (!Character.isWhitespace(chaine.charAt(i))) {
	            return false;
	        }
	    }

	    return true;
	}
	
	/**
	 * Créer l'application
	 */
	public CentralInterface() {
		initialize();
		
	}

	/**
	 * Initialiser le contenu de la frame
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("Polycentrale");
		//mainFrame.setResizable(false);
		mainFrame.setBounds(dim.width/2-1280/2, dim.height/2-720/2, 1280, 720);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		JPanel left_menu_panel = new JPanel();
		left_menu_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		left_menu_panel.setBackground(colorSideMenu);
		left_menu_panel.setBounds(0, 42, 142, 649);
		mainFrame.getContentPane().add(left_menu_panel);
		
		JLayeredPane mainPanel = new JLayeredPane();
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBounds(140, 0, 1140, 691);
		mainFrame.getContentPane().add(mainPanel);
		
		JPanel panelCheckInOut = new JPanel();
		panelCheckInOut.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCheckInOut.setBackground(Color.WHITE);
		panelCheckInOut.setBounds(1, 0, 1137, 691);
		mainPanel.add(panelCheckInOut);
		panelCheckInOut.setLayout(null);
		
		JPanel bodyPanelCheckInOut = new JPanel();
		bodyPanelCheckInOut.setBackground(colorBody);
		bodyPanelCheckInOut.setBorder(new LineBorder(new Color(0, 0, 0)));
		bodyPanelCheckInOut.setBounds(0, 42, 1133, 649);
		panelCheckInOut.add(bodyPanelCheckInOut);
		bodyPanelCheckInOut.setLayout(null);
		
		JButton buttonCreateCheckInOut = new JButton("Create CheckInOut");
		buttonCreateCheckInOut.setOpaque(true);
		buttonCreateCheckInOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int nbEmployees = 0;
				if(DataCentral.getManagers()!=null) { nbEmployees+=DataCentral.getManagers().size(); }
				if(DataCentral.getEmployees()!=null) { nbEmployees+=DataCentral.getEmployees().size(); }
				if( nbEmployees > 0 ){
					dialogPanelCreateCheckInOut(mainFrame);
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "You need at least one employee to create a checkInOut", "Unable to create CheckInOut", JOptionPane.ERROR_MESSAGE);
				}
            }});
		buttonCreateCheckInOut.setBounds(284, 590, 145, 25);
		bodyPanelCheckInOut.add(buttonCreateCheckInOut);
		
		JButton buttonDeleteCheckInOut = new JButton("Delete CheckInOut");
		buttonDeleteCheckInOut.setOpaque(true);
		buttonDeleteCheckInOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int rowIndex = tableCheckInOut.getSelectedRow();
				int colIndex = tableCheckInOut.getSelectedColumn();
				int nbCIO = 0;
				if(DataCentral.getEmployees() != null) {
					for(Employee emp : DataCentral.getEmployees()) {
						if(emp.getWorkingDays() != null) {
							nbCIO += emp.getWorkingDays().size();
						}
					}
				}
				if(DataCentral.getManagers() != null) {
					for(Manager man : DataCentral.getManagers()) {
						if(man.getWorkingDays() != null) {
							nbCIO += man.getWorkingDays().size();
						}
					}
				}
				if(DataCentral.getWorkingDays() != null) {
					nbCIO += DataCentral.getWorkingDays().size();
				}
				
				if(nbCIO > 0 ){
					if(rowIndex != -1 && colIndex != -1) {
						int reply = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to delete this checkInOut ?", "Warning", JOptionPane.YES_NO_OPTION);
				        if (reply != JOptionPane.YES_OPTION) {
				          //Then do nothing
				        }
				        else {
				        	int indexDay = 0;
							int indexDayToRemove = -1;
				        	for(Employee emp : DataCentral.getEmployees()) {
								if(((Employee) tableCheckInOut.getValueAt(rowIndex, 0)).getIdentifiant() == emp.getIdentifiant()) {
									if((CheckInOut)tableCheckInOut.getValueAt(rowIndex, 2) != null) {
										for(WorkingDay wd : emp.getWorkingDays()) {
											//Donc si on trouve le meme working day dans l'employ�, on le supprime
											if(indexDayToRemove==-1 && wd.getIdEmployee() == ((Employee) tableCheckInOut.getValueAt(rowIndex, 0)).getIdentifiant()
													&& (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 1) == wd.getStart()
													&& (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 2) == wd.getEnd())
											{
												indexDayToRemove = indexDay;
											}
											indexDay++;
										}
										if(indexDayToRemove != -1) {
											DataCentral.removeWorkingDayInEmployee(emp, indexDayToRemove);
										}
									}
									else {
										for(WorkingDay wd : DataCentral.getWorkingDays()) {
											if(indexDayToRemove==-1 && wd.getIdEmployee() == ((Employee) tableCheckInOut.getValueAt(rowIndex, 0)).getIdentifiant()
													&& (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 1) == wd.getStart()
													&& wd.getEnd()==null)
											{
												indexDayToRemove = indexDay;
											}
										}
										if(indexDayToRemove != -1) {
											DataCentral.removeWorkingDayInData(emp.getIdentifiant());
										}
									}
								}
							}
				        	//Si on a pas trouve de workingDay a supprimer dans les employes, on regarde dans les managers
				        	if(indexDayToRemove == -1) {
								indexDay = 0;
								indexDayToRemove = -1;
								
								for(Manager m : DataCentral.getManagers()) {
									indexDay = 0;
									indexDayToRemove = -1;
									if(((Employee) tableCheckInOut.getValueAt(rowIndex, 0)).getIdentifiant() == m.getIdentifiant()) {
										if((CheckInOut)tableCheckInOut.getValueAt(rowIndex, 2) != null) {
											for(WorkingDay wd : m.getWorkingDays()) {
												//Donc si on trouve le meme working day dans le manager, on le supprime
												if(indexDayToRemove == -1 && wd.getIdEmployee() == ((Employee) tableCheckInOut.getValueAt(rowIndex, 0)).getIdentifiant()
														&& (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 1) == wd.getStart()
														&& (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 2) == wd.getEnd())
												{
													indexDayToRemove = indexDay;
												}
												indexDay++;
											}
											if(indexDayToRemove != -1) {
												DataCentral.removeWorkingDayInEmployee(m, indexDayToRemove);
											}
										}
										else {
											for(WorkingDay wd : DataCentral.getWorkingDays()) {
												if(indexDayToRemove == -1 && wd.getIdEmployee() == ((Employee) tableCheckInOut.getValueAt(rowIndex, 0)).getIdentifiant()
														&& (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 1) == wd.getStart()
														&& wd.getEnd()==null)
												{
													indexDayToRemove = indexDay;
												}
											}
											if(indexDayToRemove != -1) {
												DataCentral.removeWorkingDayInData(m.getIdentifiant());
											}
										}
									}
								}
				        	}
				        	
							refreshTables();
							try {
								//	=	SAUVEGARDER DANS LE FICHIER			=
								ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
				        }
					}
					else {
						JOptionPane.showMessageDialog(mainFrame, "Please select a checkInOut before deleting it", "Unable to delete CheckInOut", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No CheckInOut to delete", "Unable to delete CheckInOut", JOptionPane.ERROR_MESSAGE);
				}
            }
		});
		buttonDeleteCheckInOut.setBounds(684, 590, 145, 25);
		bodyPanelCheckInOut.add(buttonDeleteCheckInOut);
		
		JButton buttonModifyCheckInOut = new JButton("Modify CheckInOut");
		buttonModifyCheckInOut.setOpaque(true);
		buttonModifyCheckInOut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int rowIndex = tableCheckInOut.getSelectedRow();
				int colIndex = tableCheckInOut.getSelectedColumn();
				int nbCIO = 0;
				
				if(DataCentral.getEmployees() != null) {
					for(Employee emp : DataCentral.getEmployees()) {
						if(emp.getWorkingDays() != null) {
							nbCIO += emp.getWorkingDays().size();
						}
					}
				}
				if(DataCentral.getManagers() != null) {
					for(Manager man : DataCentral.getManagers()) {
						if(man.getWorkingDays() != null) {
							nbCIO += man.getWorkingDays().size();
						}
					}
				}
				if(DataCentral.getWorkingDays() != null) {
					nbCIO += DataCentral.getWorkingDays().size();
				}
				
				if(nbCIO > 0 ){
					if(rowIndex != -1 && colIndex != -1) {
						WorkingDay wdToFind = null;
						if((CheckInOut)tableCheckInOut.getValueAt(rowIndex, 2) != null) {
							wdToFind= new WorkingDay(((Employee) tableCheckInOut.getValueAt(rowIndex, 0)).getIdentifiant(), (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 1), (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 2));
							}
						else {
							wdToFind= new WorkingDay(((Employee) tableCheckInOut.getValueAt(rowIndex, 0)).getIdentifiant(), (CheckInOut)tableCheckInOut.getValueAt(rowIndex, 1));
						}
						 //On cherche le wd dans les employes :
						for(Employee emp : DataCentral.getEmployees()) {
							if(emp.getWorkingDays().contains(wdToFind)) {
								for(WorkingDay wd : emp.getWorkingDays()) {
									if(wd.equals(wdToFind)) {
										dialogPanelModifyCheckInOut(mainFrame, 1, emp, wd);
									}
								}
							}
						}
						//On cherche le wd dans les managers :
						for(Manager man : DataCentral.getManagers()) {
							if(man.getWorkingDays().contains(wdToFind)) {
								for(WorkingDay wd : man.getWorkingDays()) {
									if(wd.equals(wd)) {
										dialogPanelModifyCheckInOut(mainFrame, 2, man, wd);
									}
								}
							}
						}
						
						//On cherche le wd dans les workingDay non completes :
						for(WorkingDay wd : DataCentral.getWorkingDays()) {
							if(wd.equals(wdToFind)) {
								//Une fois qu'on a trouve le wd correspondant, on parcourt les employes et les managers pour le trouver
								for(Employee e1 : DataCentral.getEmployees()) {
									if(e1.getIdentifiant() == wd.getIdEmployee()) {
										dialogPanelModifyCheckInOut(mainFrame, 3, e1, wd);
									}
								}
								for(Manager e1 : DataCentral.getManagers()) {
									if(e1.getIdentifiant() == wd.getIdEmployee()) {
										dialogPanelModifyCheckInOut(mainFrame, 3, e1, wd);
									}
								}
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(mainFrame, "Please select a checkInOut before modifying it", "Unable to modify CheckInOut", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No CheckInOut to modify", "Unable to modify CheckInOut", JOptionPane.ERROR_MESSAGE);
				}
            }
		});
		buttonModifyCheckInOut.setBounds(482, 590, 145, 25);
		bodyPanelCheckInOut.add(buttonModifyCheckInOut);
		
		JLabel titleCheckInOut = new JLabel("Check In/Out list :");
		titleCheckInOut.setBounds(12, 13, 105, 14);
		bodyPanelCheckInOut.add(titleCheckInOut);

		JScrollPane scrollpaneTableCheckInOut = new JScrollPane();
		scrollpaneTableCheckInOut.getViewport().setBackground(Color.WHITE);
		scrollpaneTableCheckInOut.setBounds(54, 58, 1021, 511);
		bodyPanelCheckInOut.add(scrollpaneTableCheckInOut);
		
		tableCheckInOut = new JTable(modelTableCheckInOut);
		tableCheckInOut.setAutoCreateRowSorter(true);
		tableCheckInOut.getRowSorter().toggleSortOrder(0);
		scrollpaneTableCheckInOut.setViewportView(tableCheckInOut);
		
		JButton checkInOutClearFilterButton = new JButton(" Clear filters", new ImageIcon(getClass().getClassLoader().getResource("ressources/clear_filter.png")));
		checkInOutClearFilterButton.setOpaque(true);
		checkInOutClearFilterButton.setHorizontalAlignment(SwingConstants.LEFT);
		checkInOutClearFilterButton.setBackground(colorSelected);
		checkInOutClearFilterButton.setBorder(new LineBorder(colorTopMenu));
		checkInOutClearFilterButton.setForeground(Color.WHITE);
		checkInOutClearFilterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				checkInOut_filter_type = -1;
				refreshTables();
            }
		});
		checkInOutClearFilterButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	checkInOutClearFilterButton.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	checkInOutClearFilterButton.setBackground(colorSelected);
		    }
		});
		checkInOutClearFilterButton.setBounds(859, 15, 105, 27);
		bodyPanelCheckInOut.add(checkInOutClearFilterButton);
		
		JButton checkInOutFilterButton = new JButton(" Edit filters", new ImageIcon(getClass().getClassLoader().getResource("ressources/filter.png")));
		checkInOutFilterButton.setOpaque(true);
		checkInOutFilterButton.setHorizontalAlignment(SwingConstants.LEFT);
		checkInOutFilterButton.setBackground(colorSelected);
		checkInOutFilterButton.setBorder(new LineBorder(colorTopMenu));
		checkInOutFilterButton.setForeground(Color.WHITE);
		checkInOutFilterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPanelFiltersCheckInOut(mainFrame);
            }
		});
		checkInOutFilterButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	checkInOutFilterButton.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	checkInOutFilterButton.setBackground(colorSelected);
		    }
		});
		checkInOutFilterButton.setBounds(979, 15, 95, 27);
		bodyPanelCheckInOut.add(checkInOutFilterButton);
		
		JPanel searchBarCheckInOut = new JPanel();
		searchBarCheckInOut.setLayout(null);
		searchBarCheckInOut.setBackground(colorTopMenu);
		searchBarCheckInOut.setBorder(null);
		searchBarCheckInOut.setBounds(0, 0, 1133, 43);
		panelCheckInOut.add(searchBarCheckInOut);
		
		JButton checkInOutSettingsButton = new JButton("");
		checkInOutSettingsButton.setOpaque(true);
		checkInOutSettingsButton.setBorder(null);
		checkInOutSettingsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ressources/settings.png")));
		checkInOutSettingsButton.setBackground(colorTopMenu);
		checkInOutSettingsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPanelSettings(mainFrame);
            }
		});
		checkInOutSettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	checkInOutSettingsButton.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	checkInOutSettingsButton.setBackground(colorTopMenu);
		    }
		});
		checkInOutSettingsButton.setBounds(1075, 5, 33, 33);
		searchBarCheckInOut.add(checkInOutSettingsButton);
		
		JPanel panelEmployees = new JPanel();
		panelEmployees.setBackground(Color.WHITE);
		panelEmployees.setBounds(1, 0, 1137, 691);
		mainPanel.add(panelEmployees);
		panelEmployees.setLayout(null);
		
		JPanel bodyPanelEmployees = new JPanel();
		bodyPanelEmployees.setBackground(colorBody);
		bodyPanelEmployees.setBorder(new LineBorder(new Color(0, 0, 0)));
		bodyPanelEmployees.setBounds(0, 42, 1133, 649);
		panelEmployees.add(bodyPanelEmployees);
		bodyPanelEmployees.setLayout(null);
		
		JLabel titleDisplayPanel = new JLabel("Displaying all employees :");
		titleDisplayPanel.setBounds(12, 13, 186, 16);
		bodyPanelEmployees.add(titleDisplayPanel);
		
		JScrollPane scrollpaneTableEmployees = new JScrollPane();
		scrollpaneTableEmployees.getViewport().setBackground(Color.WHITE);
		scrollpaneTableEmployees.setBounds(54, 58, 1021, 511);
		bodyPanelEmployees.add(scrollpaneTableEmployees);
		tableEmployee = new JTable(modelTableEmployee);
		tableEmployee.setBackground(Color.WHITE);
		tableEmployee.setAutoCreateRowSorter(true);
		tableEmployee.getRowSorter().toggleSortOrder(1);
		scrollpaneTableEmployees.setViewportView(tableEmployee);
		
		JButton buttonCreate = new JButton("Create employee");
		buttonCreate.setOpaque(true);
		buttonCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(DataCentral.getDepartments()!=null && DataCentral.getDepartments().size() > 0) {
					dialogPanelCreateEmployee(mainFrame);
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "You need at least one department to create an employee", "Unable to create employee", JOptionPane.ERROR_MESSAGE);
				}
            }
		});
		buttonCreate.setBounds(284, 590, 145, 25);
		bodyPanelEmployees.add(buttonCreate);
		
		JButton buttonDelete = new JButton("Delete employee");
		buttonDelete.setOpaque(true);
		buttonDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int nbEmployees = 0;
				if(DataCentral.getManagers()!=null) { nbEmployees+=DataCentral.getManagers().size(); }
				if(DataCentral.getEmployees()!=null) { nbEmployees+=DataCentral.getEmployees().size(); }
				if( nbEmployees > 0 ){
					dialogPanelDeleteEmployee(mainFrame);
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No employees to delete", "Unable to delete employee", JOptionPane.ERROR_MESSAGE);
				}
            }
		});
		buttonDelete.setBounds(684, 590, 145, 25);
		bodyPanelEmployees.add(buttonDelete);
		
		JButton buttonModifyEmployee = new JButton("Modify employee");
		buttonModifyEmployee.setOpaque(true);
		buttonModifyEmployee.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int nbEmployees = 0;
				if(DataCentral.getManagers()!=null) { nbEmployees+=DataCentral.getManagers().size(); }
				if(DataCentral.getEmployees()!=null) { nbEmployees+=DataCentral.getEmployees().size(); }
				if( nbEmployees > 0 ){
					dialogPanelSelectEmployee(mainFrame);
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No employees to edit", "Unable to edit employee", JOptionPane.ERROR_MESSAGE);
				}
            }
		});
		buttonModifyEmployee.setBounds(482, 590, 145, 25);
		bodyPanelEmployees.add(buttonModifyEmployee);
		
		JButton employeeClearFilterButton = new JButton(" Clear filters", new ImageIcon(getClass().getClassLoader().getResource("ressources/clear_filter.png")));
		employeeClearFilterButton.setOpaque(true);
		employeeClearFilterButton.setHorizontalAlignment(SwingConstants.LEFT);
		employeeClearFilterButton.setBackground(colorSelected);
		employeeClearFilterButton.setBorder(new LineBorder(colorTopMenu));
		employeeClearFilterButton.setForeground(Color.WHITE);
		employeeClearFilterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				employee_filter_type = -1;
				refreshTables();
            }
		});
		employeeClearFilterButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	employeeClearFilterButton.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	employeeClearFilterButton.setBackground(colorSelected);
		    }
		});
		employeeClearFilterButton.setBounds(859, 15, 105, 27);
		bodyPanelEmployees.add(employeeClearFilterButton);
		
		JButton employeeFilterButton = new JButton(" Edit filters", new ImageIcon(getClass().getClassLoader().getResource("ressources/filter.png")));
		employeeFilterButton.setOpaque(true);
		employeeFilterButton.setHorizontalAlignment(SwingConstants.LEFT);
		employeeFilterButton.setBackground(colorSelected);
		employeeFilterButton.setBorder(new LineBorder(colorTopMenu));
		employeeFilterButton.setForeground(Color.WHITE);
		employeeFilterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPanelFiltersEmployee(mainFrame);
            }
		});
		employeeFilterButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	employeeFilterButton.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	employeeFilterButton.setBackground(colorSelected);
		    }
		});
		employeeFilterButton.setBounds(979, 15, 95, 27);
		bodyPanelEmployees.add(employeeFilterButton);
		
		
		JPanel searchBarEmployees = new JPanel();
		searchBarEmployees.setBackground(colorTopMenu);
		searchBarEmployees.setBorder(null);
		searchBarEmployees.setBounds(0, 0, 1133, 43);
		panelEmployees.add(searchBarEmployees);
		searchBarEmployees.setLayout(null);

		JFormattedTextField searchFieldNameEmployee;
		searchFieldNameEmployee = new JFormattedTextField();
		searchFieldNameEmployee.setForeground(Color.BLACK);
		searchFieldNameEmployee.setBackground(colorTextField);
		searchFieldNameEmployee.setText("Name");
		searchFieldNameEmployee.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0){
				if(searchFieldNameEmployee.getText().equals("Name")){
					searchFieldNameEmployee.setText("");
				}
			}
			
			@Override
			public void focusLost(FocusEvent arg0){
				if(searchFieldNameEmployee.getText().isEmpty()){
					searchFieldNameEmployee.setText("Name");
				}
			}
		});
		
		JFormattedTextField antiSelectionOfFieldOnStartTextField = new JFormattedTextField();
		antiSelectionOfFieldOnStartTextField.setEditable(false);
		antiSelectionOfFieldOnStartTextField.setBounds(0, 0, 0, 0);
		searchBarEmployees.add(antiSelectionOfFieldOnStartTextField);
		
		searchFieldNameEmployee.setBounds(12, 11, 116, 22);
		searchBarEmployees.add(searchFieldNameEmployee);
		searchFieldNameEmployee.setToolTipText("Search");
		searchFieldNameEmployee.setColumns(10);
		
		JFormattedTextField searchFieldSurnameEmployee = new JFormattedTextField();
		searchFieldSurnameEmployee.setForeground(Color.BLACK);
		searchFieldSurnameEmployee.setBackground(colorTextField);
		searchFieldSurnameEmployee.setText("Surname");
		searchFieldSurnameEmployee.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0){
				if(searchFieldSurnameEmployee.getText().equals("Surname")){
					searchFieldSurnameEmployee.setText("");
				}
			}
			
			@Override
			public void focusLost(FocusEvent arg0){
				if(searchFieldSurnameEmployee.getText().isEmpty()){
					searchFieldSurnameEmployee.setText("Surname");
				}
			}
		});
		searchFieldSurnameEmployee.setBounds(136, 11, 116, 22);
		searchBarEmployees.add(searchFieldSurnameEmployee);
		
		JButton employeesSearchButton = new JButton("");
		employeesSearchButton.setOpaque(true);
		employeesSearchButton.setBackground(Color.WHITE);
		employeesSearchButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ressources/search.png")));
		employeesSearchButton.setBounds(259, 11, 22, 22);
		employeesSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//On récupère uniquement les employés correspondants au texte des JFormattedTextField :
				listTableEmployeeAfterResearch = Employee.searchEmployees(listTableEmployee, searchFieldNameEmployee.getText(), searchFieldSurnameEmployee.getText());
				
				//Puis on change le contenu de la table des employés et on l'actualise :
				modelTableEmployee.setListEmployees(listTableEmployeeAfterResearch);
				modelTableEmployee.fireTableDataChanged();
			}
		});
		searchFieldNameEmployee.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//On récupère uniquement les employés correspondants au texte des JFormattedTextField :
				listTableEmployeeAfterResearch = Employee.searchEmployees(listTableEmployee, searchFieldNameEmployee.getText(), searchFieldSurnameEmployee.getText());
				
				//Puis on change le contenu de la table des employés et on l'actualise :
				modelTableEmployee.setListEmployees(listTableEmployeeAfterResearch);
				modelTableEmployee.fireTableDataChanged();
            }
		});
		searchFieldSurnameEmployee.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//On récupère uniquement les employés correspondants au texte des JFormattedTextField :
				listTableEmployeeAfterResearch = Employee.searchEmployees(listTableEmployee, searchFieldNameEmployee.getText(), searchFieldSurnameEmployee.getText());
				
				//Puis on change le contenu de la table des employés et on l'actualise :
				modelTableEmployee.setListEmployees(listTableEmployeeAfterResearch);
				modelTableEmployee.fireTableDataChanged();
            }
		});
		searchBarEmployees.add(employeesSearchButton);
		
		JButton employeesSettingsButton = new JButton("");
		employeesSettingsButton.setOpaque(true);
		employeesSettingsButton.setBorder(null);
		employeesSettingsButton.setBackground(colorTopMenu);
		employeesSettingsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ressources/settings.png")));
		employeesSettingsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPanelSettings(mainFrame);
            }
		});
		employeesSettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	employeesSettingsButton.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	employeesSettingsButton.setBackground(colorTopMenu);
		    }
		});
		
		employeesSettingsButton.setBounds(1075, 5, 33, 33);
		searchBarEmployees.add(employeesSettingsButton);
		
		JButton mockCompany = new JButton("Create company", new ImageIcon(getClass().getClassLoader().getResource("ressources/new_small.png")));
		mockCompany.setOpaque(true);
		mockCompany.setHorizontalAlignment(SwingConstants.LEFT);
		mockCompany.setBackground(colorSelected);
		mockCompany.setBorder(new LineBorder(colorUnselected));
		mockCompany.setBorderPainted(false);
		mockCompany.setFocusPainted(false);
		mockCompany.setForeground(Color.WHITE);
		mockCompany.setBounds(940, 11, 130, 22);
		searchBarEmployees.add(mockCompany);
		mockCompany.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	mockCompany.setBackground(colorHover);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	mockCompany.setBackground(colorSelected);
		    }
		});
		mockCompany.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int reply = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to erase previous data and create a new basic company ?", "Warning", JOptionPane.YES_NO_OPTION);
		        if (reply == JOptionPane.YES_OPTION) {
		        	Central.mockCompany();
					refreshTables();
		        }
			}
		});
		
		JPanel panelDepartments = new JPanel();
		panelDepartments.setBackground(Color.WHITE);
		panelDepartments.setBounds(1, 0, 1137, 691);
		mainPanel.add(panelDepartments);
		panelDepartments.setLayout(null);
		
		JPanel searchBarDepartments = new JPanel();
		searchBarDepartments.setLayout(null);
		searchBarDepartments.setBorder(null);
		searchBarDepartments.setBackground(colorTopMenu);
		searchBarDepartments.setBounds(0, 0, 1133, 43);
		panelDepartments.add(searchBarDepartments);
		
		JFormattedTextField textFieldNameDepartment = new JFormattedTextField();
		textFieldNameDepartment.setForeground(Color.BLACK);
		textFieldNameDepartment.setBackground(colorTextField);
		textFieldNameDepartment.setToolTipText("Search");
		textFieldNameDepartment.setText("Name");
		textFieldNameDepartment.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0){
				if(textFieldNameDepartment.getText().equals("Name")){
					textFieldNameDepartment.setText("");
				}
			}
			
			@Override
			public void focusLost(FocusEvent arg0){
				if(textFieldNameDepartment.getText().isEmpty()){
					textFieldNameDepartment.setText("Name");
				}
			}
		});
		textFieldNameDepartment.setColumns(10);
		textFieldNameDepartment.setBounds(12, 11, 116, 22);
		searchBarDepartments.add(textFieldNameDepartment);
		
		JButton searchButtonDepartments = new JButton("");
		searchButtonDepartments.setOpaque(true);
		searchButtonDepartments.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ressources/search.png")));
		searchButtonDepartments.setBackground(Color.WHITE);
		searchButtonDepartments.setBounds(135, 11, 22, 22);
		searchBarDepartments.add(searchButtonDepartments);
		searchButtonDepartments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				modelTableDepartments.setDepartments(DataCentral.searchDepartments(textFieldNameDepartment.getText()));
				modelTableDepartments.fireTableDataChanged();
			}
		});
		textFieldNameDepartment.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				modelTableDepartments.setDepartments(DataCentral.searchDepartments(textFieldNameDepartment.getText()));
				modelTableDepartments.fireTableDataChanged();
            }
		});
		
		JButton departmentsSettingsButton = new JButton("");
		departmentsSettingsButton.setOpaque(true);
		departmentsSettingsButton.setBorder(null);
		departmentsSettingsButton.setBackground(colorTopMenu);
		departmentsSettingsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ressources/settings.png")));
		departmentsSettingsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPanelSettings(mainFrame);
            }
		});
		departmentsSettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	departmentsSettingsButton.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	departmentsSettingsButton.setBackground(colorTopMenu);
		    }
		});
		departmentsSettingsButton.setBounds(1075, 5, 33, 33);
		searchBarDepartments.add(departmentsSettingsButton);
		
		JPanel panelBodyDepartments = new JPanel();
		panelBodyDepartments.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelBodyDepartments.setBounds(0, 42, 1133, 649);
		panelDepartments.add(panelBodyDepartments);
		panelBodyDepartments.setLayout(null);
		panelBodyDepartments.setBackground(colorBody);
		
		JLabel titre_panel_departements = new JLabel("List of all the departments :");
		titre_panel_departements.setBounds(12, 13, 271, 16);
		panelBodyDepartments.add(titre_panel_departements);
		
		JScrollPane scrollPaneTableDepartments = new JScrollPane();
		scrollPaneTableDepartments.getViewport().setBackground(Color.WHITE);
		scrollPaneTableDepartments.setBounds(54, 58, 1021, 511);
		panelBodyDepartments.add(scrollPaneTableDepartments);
		
		tableDepartments = new JTable(modelTableDepartments);
		tableDepartments.setAutoCreateRowSorter(true);
		tableDepartments.getRowSorter().toggleSortOrder(0);
		scrollPaneTableDepartments.setViewportView(tableDepartments);
		
		JButton buttonAddDepartment = new JButton("Create department");
		buttonAddDepartment.setOpaque(true);
		buttonAddDepartment.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPanelCreateDepartment(mainFrame);
            }
		});
		buttonAddDepartment.setBounds(276, 590, 169, 25);
		panelBodyDepartments.add(buttonAddDepartment);
		
		JButton buttonDeleteDepartment = new JButton("Delete department");
		buttonDeleteDepartment.setOpaque(true);
		buttonDeleteDepartment.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(DataCentral.getDepartments().size() > 0 ){
					dialogPanelDeleteDepartment(mainFrame);
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No department to delete", "Unable to delete department", JOptionPane.ERROR_MESSAGE);
				}
            }
		});
		buttonDeleteDepartment.setBounds(687, 590, 169, 25);
		panelBodyDepartments.add(buttonDeleteDepartment);
		
		JButton buttonModifyDepartment = new JButton("Modify department");
		buttonModifyDepartment.setOpaque(true);
		buttonModifyDepartment.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(DataCentral.getDepartments()!=null && DataCentral.getDepartments().size() == 0 ){
					JOptionPane.showMessageDialog(mainFrame, "No department to modify", "Unable to modify department", JOptionPane.ERROR_MESSAGE);
					
				}else {
					if((DataCentral.getEmployees()!=null || DataCentral.getManagers()!=null) && (DataCentral.getEmployees().size() + DataCentral.getManagers().size() == 0)) {
						JOptionPane.showMessageDialog(mainFrame, "No employee to be manager of the department", "Unable to modify department", JOptionPane.ERROR_MESSAGE);
					}
					else{
						dialogPanelModifyDepartment(mainFrame, DataCentral.getDepartments().get(0));
					}
				}
            }
		});
		buttonModifyDepartment.setBounds(482, 590, 169, 25);
		panelBodyDepartments.add(buttonModifyDepartment);
		
		JButton departmentClearFilterButton = new JButton(" Clear filters", new ImageIcon(getClass().getClassLoader().getResource("ressources/clear_filter.png")));
		departmentClearFilterButton.setOpaque(true);
		departmentClearFilterButton.setHorizontalAlignment(SwingConstants.LEFT);
		departmentClearFilterButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				modelTableDepartments.setDepartments(DataCentral.getDepartments());
				modelTableDepartments.fireTableDataChanged();
            }
		});
		departmentClearFilterButton.setForeground(Color.WHITE);
		departmentClearFilterButton.setBorder(new LineBorder(colorTopMenu));
		departmentClearFilterButton.setBackground(new Color(45, 156, 202));
		departmentClearFilterButton.setBounds(969, 15, 105, 27);
		panelBodyDepartments.add(departmentClearFilterButton);
		
		JPanel paneEvent = new JPanel();
		paneEvent.setBorder(new LineBorder(new Color(0, 0, 0)));
		paneEvent.setBackground(Color.WHITE);
		paneEvent.setBounds(1, 0, 1137, 691);
		mainPanel.add(paneEvent);
		paneEvent.setLayout(null);
		
		JPanel bodyPanelEvent = new JPanel();
		bodyPanelEvent.setBackground(colorBody);
		bodyPanelEvent.setBorder(new LineBorder(new Color(0, 0, 0)));
		bodyPanelEvent.setBounds(0, 42, 1133, 649);
		paneEvent.add(bodyPanelEvent);
		bodyPanelEvent.setLayout(null);
		
		JLabel titlePanelEvents = new JLabel("List of the late, early or missing check i/o :");
		titlePanelEvents.setBounds(12, 13, 300, 16);
		bodyPanelEvent.add(titlePanelEvents);
		
		JScrollPane scrollpaneTableEvent = new JScrollPane();
		scrollpaneTableEvent.getViewport().setBackground(Color.WHITE);
		scrollpaneTableEvent.setBounds(54, 58, 1021, 511);
		bodyPanelEvent.add(scrollpaneTableEvent);
		
		tableEvent = new JTable(modelTableEvents);
		tableEvent.setAutoCreateRowSorter(true);
		tableEvent.getRowSorter().toggleSortOrder(0);
		scrollpaneTableEvent.setViewportView(tableEvent);
		
		JButton buttonRegularize = new JButton("Regularize event");
		buttonRegularize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int rowIndex = tableEvent.getSelectedRow();
				int colIndex = tableEvent.getSelectedColumn();
				if(DataCentral.getEvents()!=null && DataCentral.getEvents().size() > 0 ){
					if(rowIndex != -1 && colIndex != -1) {
						CheckInOut CIOToFind = (CheckInOut)tableEvent.getValueAt(rowIndex, 2);
						Event event = (Event)tableEvent.getValueAt(rowIndex,  1);
						
						switch(event.getEventType()) {
							case start:
								//On cherche le CheckInOut dans les employes :
								for(Employee emp : DataCentral.getEmployees()) {
									if(emp.getWorkingDays() != null) {
										for(WorkingDay wd : emp.getWorkingDays()) {
											if(wd.getStart().equals(CIOToFind)) {
												dialogPanelModifyCheckInOut(mainFrame, 1, emp, wd);
											}
										}
									}
								}
								//On cherche le CheckInOut dans les managers :
								for(Manager man : DataCentral.getManagers()) {
									if(man.getWorkingDays() != null) {
										for(WorkingDay wd : man.getWorkingDays()) {
											if(wd.getStart().equals(CIOToFind)) {
												dialogPanelModifyCheckInOut(mainFrame, 2, man, wd);
											}
										}
									}
								}
								//On cherche le CheckInOut dans les workingDay non completes :
								for(WorkingDay wd : DataCentral.getWorkingDays()) {
									if(wd.getStart().equals(CIOToFind)) {
										//Une fois qu'on a trouve le wd correspondant, on parcourt les employes et les managers pour le trouver
										for(Employee e1 : DataCentral.getEmployees()) {
											if(e1.getIdentifiant() == wd.getIdEmployee()) {
												dialogPanelModifyCheckInOut(mainFrame, 3, e1, wd);
											}
										}
										for(Manager e1 : DataCentral.getManagers()) {
											if(e1.getIdentifiant() == wd.getIdEmployee()) {
												dialogPanelModifyCheckInOut(mainFrame, 3, e1, wd);
											}
										}
									}
								}
					    		break;
					    		
					    	case end:
								//On cherche le CheckInOut dans les employes :
								for(Employee emp : DataCentral.getEmployees()) {
									if(emp.getWorkingDays() != null) {
										for(WorkingDay wd : emp.getWorkingDays()) {
											if(wd.getEnd().equals(CIOToFind)) {
												dialogPanelModifyCheckInOut(mainFrame, 1, emp, wd);
											}
										}
									}
								}
								//On cherche le CheckInOut dans les managers :
								for(Manager man : DataCentral.getManagers()) {
									if(man.getWorkingDays() != null) {
										for(WorkingDay wd : man.getWorkingDays()) {
											if(wd.getEnd().equals(CIOToFind)) {
												dialogPanelModifyCheckInOut(mainFrame, 2, man, wd);
											}
										}
									}
								}
								//On cherche le CheckInOut dans les workingDay non completes :
								for(WorkingDay wd : DataCentral.getWorkingDays()) {
									if(wd.getEnd().equals(CIOToFind)) {
										//Une fois qu'on a trouve le wd correspondant, on parcourt les employes et les managers pour le trouver
										for(Employee e1 : DataCentral.getEmployees()) {
											if(e1.getIdentifiant() == wd.getIdEmployee()) {
												dialogPanelModifyCheckInOut(mainFrame, 3, e1, wd);
											}
										}
										for(Manager e1 : DataCentral.getManagers()) {
											if(e1.getIdentifiant() == wd.getIdEmployee()) {
												dialogPanelModifyCheckInOut(mainFrame, 3, e1, wd);
											}
										}
									}
								}
					    		break;
					    		
					    	case day:
					    		//Cas à traiter si jamais l'implémentation d'un calendrier de travail des employés est fait...
					    		break;
					    		
							default:
								//Cas impossible normalement
								break;
						}
					}
					else {
						JOptionPane.showMessageDialog(mainFrame, "Please select an event before regularizing it", "Unable to regularize event", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No event to regularize", "Unable to regularize event", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonRegularize.setBounds(284, 590, 139, 25);
		bodyPanelEvent.add(buttonRegularize);
		
		JButton buttonDeleteAll = new JButton("Delete all events");
		buttonDeleteAll.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(DataCentral.getEvents()!=null && DataCentral.getEvents().size() > 0) {
					int reply = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to delete all of the events ?", "Warning", JOptionPane.YES_NO_OPTION);
			        if (reply == JOptionPane.YES_OPTION) {
			        	DataCentral.getEvents().clear();
						refreshTables();
						
						try {
							//	=	SAUVEGARDER DANS LE FICHIER		=
							ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
						}
						catch (IOException e1) {
							e1.printStackTrace();
						}
			        }
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No event to delete.", "Unable to delete all events", JOptionPane.ERROR_MESSAGE);
				}
            }
		});
		
		JButton buttonIgnore = new JButton("Ignore event");
		buttonIgnore.setBounds(482, 590, 124, 25);
		bodyPanelEvent.add(buttonIgnore);
		buttonDeleteAll.setBounds(684, 590, 131, 25);
		bodyPanelEvent.add(buttonDeleteAll);
		buttonIgnore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int rowIndex = tableEvent.getSelectedRow();
				int colIndex = tableEvent.getSelectedColumn();
				if(DataCentral.getEvents()!=null && DataCentral.getEvents().size() > 0 ){
					if(rowIndex != -1 && colIndex != -1) {
						int reply = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to ignore this event ?", "Warning", JOptionPane.YES_NO_OPTION);
				        if (reply == JOptionPane.YES_OPTION) {
				        	Event event = (Event)tableEvent.getValueAt(rowIndex,  1);
							DataCentral.getEvents().remove(event);
							refreshTables();
							try {
								//	=	SAUVEGARDER DANS LE FICHIER			=
								ReadWriteCentral.saveDataCentral(DataCentral.adressSaveDataCentral);
							} catch (IOException e2) {
								e2.printStackTrace();
							}
				        }
					}
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No event to ignore.", "Unable to ignore event", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JPanel searchBarEvents = new JPanel();
		searchBarEvents.setLayout(null);
		searchBarEvents.setBorder(null);
		searchBarEvents.setBackground(colorTopMenu);
		searchBarEvents.setBounds(0, 0, 1133, 43);
		paneEvent.add(searchBarEvents);
		
		JButton eventsSettingsButton = new JButton("");
		eventsSettingsButton.setOpaque(true);
		eventsSettingsButton.setBorder(null);
		eventsSettingsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ressources/settings.png")));
		eventsSettingsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPanelSettings(mainFrame);
            }
		});
		eventsSettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	eventsSettingsButton.setBackground(colorHover);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	eventsSettingsButton.setBackground(colorTopMenu);
		    }
		});
		eventsSettingsButton.setBackground(colorTopMenu);
		eventsSettingsButton.setBounds(1075, 5, 33, 33);
		searchBarEvents.add(eventsSettingsButton);
		
		JButton buttonEmployees = new JButton("  Employees", new ImageIcon(getClass().getClassLoader().getResource("ressources/employee.png")));
		buttonEmployees.setOpaque(true);
		buttonEmployees.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton buttonCheckInOut = new JButton("  Check In Out", new ImageIcon(getClass().getClassLoader().getResource("ressources/badge.png")));
		buttonCheckInOut.setOpaque(true);
		buttonCheckInOut.setHorizontalAlignment(SwingConstants.LEFT);
		
		JButton buttonDepartments = new JButton("  Departments", new ImageIcon(getClass().getClassLoader().getResource("ressources/department.png")));
		buttonDepartments.setOpaque(true);
		buttonDepartments.setHorizontalAlignment(SwingConstants.LEFT);
		
		buttonEvents = new JButton("  Events", new ImageIcon(getClass().getClassLoader().getResource("ressources/warning.png")));
		buttonEvents.setOpaque(true);
		buttonEvents.setHorizontalAlignment(SwingConstants.LEFT);
		
		buttonIncoherences = new JButton("  Incoherences (0)", new ImageIcon(getClass().getClassLoader().getResource("ressources/incoherence.png")));
		buttonIncoherences.setOpaque(true);
		buttonIncoherences.setHorizontalAlignment(SwingConstants.LEFT);
		
		buttonEmployees.setBackground(colorSelected);
		buttonEmployees.setBorder(new LineBorder(colorUnselected));
		buttonEmployees.setBorderPainted(false);
		buttonEmployees.setFocusPainted(false);
		buttonEmployees.setForeground(Color.WHITE);
		buttonEmployees.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	buttonEmployees.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	if(selectedFrame != FRAME_EMPLOYEES) {
		    		buttonEmployees.setBackground(colorUnselected);
		    	}
		    	else {
		    		buttonEmployees.setBackground(colorSelected);
		    	}
		    }
		});
		buttonEmployees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Changer la couleur des boutons :
				selectedFrame = FRAME_EMPLOYEES;
				buttonEmployees.setBackground(colorSelected);
				buttonCheckInOut.setBackground(colorUnselected);
				buttonDepartments.setBackground(colorUnselected);
				buttonEvents.setBackground(colorButtonEvents);
				buttonIncoherences.setBackground(colorButtonIncoherences);
				
				//Enlever le/les panel(s) d'avant et ajouter le panel que l'on veut afficher :
				mainPanel.removeAll();
				mainPanel.add(panelEmployees);
				mainPanel.repaint();
				mainPanel.revalidate();
			}
		});
		buttonEmployees.setBounds(1, 1, 140, 30);
		
		
		buttonCheckInOut.setBackground(colorUnselected);
		buttonCheckInOut.setBorder(new LineBorder(colorUnselected));
		buttonCheckInOut.setBorderPainted(false);
		buttonCheckInOut.setFocusPainted(false);
		buttonCheckInOut.setForeground(Color.WHITE);
		buttonCheckInOut.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	buttonCheckInOut.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	if(selectedFrame != FRAME_CHECKINOUT) {
		    		buttonCheckInOut.setBackground(colorUnselected);
		    	}
		    	else {
		    		buttonCheckInOut.setBackground(colorSelected);
		    	}
		    }
		});
		buttonCheckInOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Changer la couleur des boutons :
				selectedFrame = FRAME_CHECKINOUT;
	    		buttonEmployees.setBackground(colorUnselected);
				buttonCheckInOut.setBackground(colorSelected);
	    		buttonDepartments.setBackground(colorUnselected);
				buttonEvents.setBackground(colorButtonEvents);
				buttonIncoherences.setBackground(colorButtonIncoherences);
				
				//Enlever le/les panel(s) d'avant et ajouter le panel que l'on veut afficher :
				mainPanel.removeAll();
				mainPanel.add(panelCheckInOut);
				mainPanel.repaint();
				mainPanel.revalidate();
			}
		});
		buttonCheckInOut.setBounds(1, 32, 140, 30);
		
		buttonDepartments.setBackground(colorUnselected);
		buttonDepartments.setBorder(new LineBorder(colorUnselected));
		buttonDepartments.setBorderPainted(false);
		buttonDepartments.setFocusPainted(false);
		buttonDepartments.setForeground(Color.WHITE);
		buttonDepartments.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	buttonDepartments.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	if(selectedFrame != FRAME_DEPARTMENTS) {
		    		buttonDepartments.setBackground(colorUnselected);
		    	}
		    	else {
		    		buttonDepartments.setBackground(colorSelected);
		    	}
		    }
		});
		buttonDepartments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Changer la couleur des boutons :
				selectedFrame = FRAME_DEPARTMENTS;
				buttonEmployees.setBackground(colorUnselected);
				buttonCheckInOut.setBackground(colorUnselected);
				buttonDepartments.setBackground(colorSelected);
				buttonEvents.setBackground(colorButtonEvents);
				buttonIncoherences.setBackground(colorButtonIncoherences);
				
				//Enlever le/les panel(s) d'avant et ajouter le panel que l'on veut afficher :
				mainPanel.removeAll();
				mainPanel.add(panelDepartments);
				mainPanel.repaint();
				mainPanel.revalidate();
			}
		});
		buttonDepartments.setBounds(1, 63, 140, 30);
		
		buttonEvents.setBackground(colorButtonEvents);
		buttonEvents.setBorder(new LineBorder(colorUnselected));
		buttonEvents.setBorderPainted(false);
		buttonEvents.setFocusPainted(false);
		buttonEvents.setForeground(Color.WHITE);
		buttonEvents.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	buttonEvents.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	if(selectedFrame != FRAME_EVENTS) {
		    		buttonEvents.setBackground(colorButtonEvents);
		    	}
		    	else {
		    		buttonEvents.setBackground(colorSelected);
		    	}
		    }
		});
		buttonEvents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Changer la couleur des boutons :
				selectedFrame = FRAME_EVENTS;
				buttonEmployees.setBackground(colorUnselected);
				buttonCheckInOut.setBackground(colorUnselected);
				buttonDepartments.setBackground(colorUnselected);
				buttonEvents.setBackground(colorButtonEvents);
				buttonIncoherences.setBackground(colorButtonIncoherences);
				
				//Enlever le/les panel(s) d'avant et ajouter le panel que l'on veut afficher :
				mainPanel.removeAll();
				mainPanel.add(paneEvent);
				mainPanel.repaint();
				mainPanel.revalidate();
			}
		});
		buttonEvents.setBounds(1, 94, 140, 30);
		
		JPanel panelIncoherences = new JPanel();
		panelIncoherences.setBounds(1, 0, 1137, 691);
		mainPanel.add(panelIncoherences);
		panelIncoherences.setLayout(null);
		
		JPanel bodyPanelIncoherences = new JPanel();
		bodyPanelIncoherences.setBackground(colorBody);
		bodyPanelIncoherences.setBorder(new LineBorder(new Color(0, 0, 0)));
		bodyPanelIncoherences.setBounds(0, 42, 1133, 649);
		panelIncoherences.add(bodyPanelIncoherences);
		bodyPanelIncoherences.setLayout(null);
		
		JLabel titleIncoherencesPanel = new JLabel("List of all the incoherences detected :");
		titleIncoherencesPanel.setBounds(12, 13, 220, 14);
		bodyPanelIncoherences.add(titleIncoherencesPanel);
		
		JScrollPane scrollPaneTableIncoherences = new JScrollPane();
		scrollPaneTableIncoherences.getViewport().setBackground(Color.WHITE);
		
		JButton buttonRegularizeIncoherence = new JButton("Regularize incoherence");
		buttonRegularizeIncoherence.setBounds(450, 590, 185, 23);
		bodyPanelIncoherences.add(buttonRegularizeIncoherence);
		scrollPaneTableIncoherences.setBounds(54, 58, 1021, 511);
		bodyPanelIncoherences.add(scrollPaneTableIncoherences);
		
		tableIncoherences = new JTable(modelTableIncoherences);
		tableIncoherences.setAutoCreateRowSorter(true);
		tableIncoherences.getRowSorter().toggleSortOrder(0);
		scrollPaneTableIncoherences.setViewportView(tableIncoherences);
		
		buttonRegularizeIncoherence.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int rowIndex = tableIncoherences.getSelectedRow();
				int colIndex = tableIncoherences.getSelectedColumn();
				if(DataCentral.getIncoherences().size() > 0 ){
					if(rowIndex != -1 && colIndex != -1) {
						
						Incoherence incoherence = (Incoherence)tableIncoherences.getValueAt(rowIndex, 1);
						
						boolean found = false;
						
						if(found==false && incoherence.getIncoherentObject() instanceof Employee) {
							found = true;
							dialogPanelModifyEmployee(mainFrame, (Employee)incoherence.getIncoherentObject());
						}
						
				        if(found==false && incoherence.getIncoherentObject() instanceof Manager) {
							found = true;
				        	dialogPanelModifyEmployee(mainFrame, (Manager)incoherence.getIncoherentObject());
				        }
				        
				        if(found==false && incoherence.getIncoherentObject() instanceof CheckInOut) {
							found = true;
				        	CheckInOut CIOToFind = (CheckInOut)incoherence.getIncoherentObject();
				        	//On cherche le CheckInOut dans les employes :
							for(Employee emp : DataCentral.getEmployees()) {
								if(emp.getWorkingDays() != null) {
									for(WorkingDay wd : emp.getWorkingDays()) {
										if(wd.getStart().equals(CIOToFind)) {
											dialogPanelModifyCheckInOut(mainFrame, 1, emp, wd);
										}
									}
								}
							}
							//On cherche le CheckInOut dans les managers :
							for(Manager man : DataCentral.getManagers()) {
								if(man.getWorkingDays() != null) {
									for(WorkingDay wd : man.getWorkingDays()) {
										if(wd.getStart().equals(CIOToFind)) {
											dialogPanelModifyCheckInOut(mainFrame, 2, man, wd);
										}
									}
								}
							}
							//On cherche le CheckInOut dans les workingDay non completes :
							for(WorkingDay wd : DataCentral.getWorkingDays()) {
								if(wd.getStart().equals(CIOToFind)) {
									//Une fois qu'on a trouve le wd correspondant, on parcourt les employes et les managers pour le trouver
									for(Employee e1 : DataCentral.getEmployees()) {
										if(e1.getIdentifiant() == wd.getIdEmployee()) {
											dialogPanelModifyCheckInOut(mainFrame, 3, e1, wd);
										}
									}
									for(Manager e1 : DataCentral.getManagers()) {
										if(e1.getIdentifiant() == wd.getIdEmployee()) {
											dialogPanelModifyCheckInOut(mainFrame, 3, e1, wd);
										}
									}
								}
							}
				        }
				        if(found==false && incoherence.getIncoherentObject() instanceof Department) {
							found = true;
				        	dialogPanelModifyDepartment(mainFrame, (Department)incoherence.getIncoherentObject());
				        }
					}
					else {
						JOptionPane.showMessageDialog(mainFrame, "Please select an incoherence before regularizing it", "Unable to regularize incoherence", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(mainFrame, "No incoherence to regularize", "Unable to regularize event", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JPanel searchBarIncoherences = new JPanel();
		searchBarIncoherences.setLayout(null);
		searchBarIncoherences.setBorder(null);
		searchBarIncoherences.setBackground(colorTopMenu);
		searchBarIncoherences.setBounds(0, 0, 1133, 43);
		panelIncoherences.add(searchBarIncoherences);
		
		JButton incoherencesSettingsButton = new JButton("");
		incoherencesSettingsButton.setOpaque(true);
		incoherencesSettingsButton.setBorder(null);
		incoherencesSettingsButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ressources/settings.png")));
		incoherencesSettingsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPanelSettings(mainFrame);
            }
		});
		incoherencesSettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	incoherencesSettingsButton.setBackground(colorHover);
		    }
		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	incoherencesSettingsButton.setBackground(colorTopMenu);
		    }
		});
		incoherencesSettingsButton.setBackground(colorTopMenu);
		incoherencesSettingsButton.setBounds(1075, 5, 33, 33);
		searchBarIncoherences.add(incoherencesSettingsButton);
		buttonIncoherences.setBackground(colorUnselected);
		buttonIncoherences.setBorder(new LineBorder(colorUnselected));
		buttonIncoherences.setBorderPainted(false);
		buttonIncoherences.setFocusPainted(false);
		buttonIncoherences.setForeground(Color.WHITE);
		buttonIncoherences.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseEntered(java.awt.event.MouseEvent evt) {
		    	buttonIncoherences.setBackground(colorHover);
		    }

		    public void mouseExited(java.awt.event.MouseEvent evt) {
		    	if(selectedFrame != FRAME_INCOHERENCES) {
		    		buttonIncoherences.setBackground(colorButtonIncoherences);
		    	}
		    	else {
		    		buttonIncoherences.setBackground(colorSelected);
		    	}
		    }
		});
		buttonIncoherences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Changer la couleur des boutons :
				selectedFrame = FRAME_INCOHERENCES;
				buttonEmployees.setBackground(colorUnselected);
				buttonCheckInOut.setBackground(colorUnselected);
				buttonDepartments.setBackground(colorUnselected);
				buttonEvents.setBackground(colorButtonEvents);
				buttonIncoherences.setBackground(colorButtonIncoherences);
				//Enlever le/les panel(s) d'avant et ajouter le panel que l'on veut afficher :
				mainPanel.removeAll();
				mainPanel.add(panelIncoherences);
				mainPanel.repaint();
				mainPanel.revalidate();
			}
		});
		buttonIncoherences.setBounds(1, 125, 140, 30);
		
		left_menu_panel.setLayout(null);
		left_menu_panel.add(buttonEmployees);
		left_menu_panel.add(buttonCheckInOut);
		left_menu_panel.add(buttonDepartments);
		left_menu_panel.add(buttonEvents);
		left_menu_panel.add(buttonIncoherences);
		
		//Chargement de la police de l'horloge (Si échec, la police est TimesRoman)
		Font customFont = new Font("TimesRoman", Font.PLAIN, 32);
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try (InputStream stream = loader.getResourceAsStream("ressources/digital-clock-font.ttf")) {
			customFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(Font.PLAIN, 32);
        } catch (FontFormatException | IOException ex) {
            System.out.println("Clock font couldn't be loaded.");
        }
		
		sideMenuTimeLabel = new JLabel("");
		sideMenuTimeLabel.setForeground(Color.WHITE);
		sideMenuTimeLabel.setBounds(5, 585, 140, 36);
		left_menu_panel.add(sideMenuTimeLabel);
		sideMenuTimeLabel.setFont(customFont);
		
		JLabel logoPolytech = new JLabel();
		logoPolytech.setVerticalAlignment(SwingConstants.TOP);
		logoPolytech.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ressources/logo-polytech-black.png")));
		logoPolytech.setBackground(Color.BLACK);
		logoPolytech.setBounds(0, 0, 141, 42);
		mainFrame.getContentPane().add(logoPolytech);
		ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("ressources/logo.png"));
		mainFrame.setIconImage(logo.getImage());
		
		//CE QUI SERA AFFICHE AU LANCEMENT :
		//On met à jour une première fois les tables
		refreshTables();
		//Enlever le/les panel(s) déjà présents
		mainPanel.removeAll();
		
		//Ajouter le panel des employés
		mainPanel.add(panelEmployees);
		
		//Actualiser la frame
		mainPanel.repaint();
		mainPanel.revalidate();
		
		//Lancer l'horloge
		CentralClock clock1 = new CentralClock();
		clock1.start();
	}
}
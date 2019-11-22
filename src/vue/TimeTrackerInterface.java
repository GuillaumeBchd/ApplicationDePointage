package vue;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import controleur.DataTimeTracker;
import controleur.DateTimeTools;
import controleur.ReadWriteTimeTracker;
import controleur.TCP.TimeTrackerClient;
import modele.Actors.Employee;
import modele.TimeRelated.CheckInOut;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;

/**
 * Interface du TimeTracker.
 * Cette classe implémente également la classe Runnable, de sorte que l'horloge se mette à jour.
 *
 */
public class TimeTrackerInterface extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	
	Thread thread = null;
	LocalDateTime localDateTime;
	LocalDateTime roundedDateTime;
	JLabel dateText;
	JLabel timeText;
	static JComboBox<Employee> employeeList;

	/**
	 * Remplace la liste des employés de TimeTrackerInterface par une nouvelle.
	 * @param listEmployees
	 */
	public static void UpdateEmployees(List<Employee> listEmployees){
		employeeList.removeAllItems();
		for(Employee e : listEmployees){
			employeeList.addItem(e);
		}
	}
	
	/**
	 * Méthode utilisée par l'horloge qui met à jour la date et l'heure des labels de TimeTrackerInterface.
	 */
	public void SetLabelsText() {
		localDateTime = LocalDateTime.now();
		roundedDateTime = DateTimeTools.roundHour(localDateTime);

		dateText.setText(localDateTime.getDayOfMonth() + " " + localDateTime.getMonth() + " " + localDateTime.getYear());

		timeText.setText(localDateTime.getHour() + " : " + localDateTime.getMinute() + " : " + localDateTime.getSecond() + " → Let's say " + roundedDateTime.getHour() + " : " + roundedDateTime.getMinute());
	}
	
	/**
	 * Interface de la classe TimeTrackerInterface.
	 */
	public TimeTrackerInterface() {
		//Definition du Panel :
		JFrame frame = new JFrame();
		Container panel = frame.getContentPane();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);
        panel.setBackground(Color.white);
        
        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("ressources/logo.png"));
        frame.setIconImage(logo.getImage());
        
		dateText = new JLabel();
		timeText = new JLabel();
		employeeList = new JComboBox<Employee>(); 
        
		JLabel img = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("ressources/logo-polytech-pointeuse.png")));
        
        JButton cio = new JButton("Check In/Out");
        cio.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(employeeList.getSelectedItem() != null) {
					Employee selectedEmployee = (Employee) employeeList.getSelectedItem();
					DataTimeTracker.addCheckInOut(new CheckInOut(DateTimeTools.roundHour(LocalDateTime.now()), selectedEmployee.getIdentifiant()));
					try {
						ReadWriteTimeTracker.saveTimeTracker(DataTimeTracker.adressSaveTimeTracker);
					} catch (IOException e1) { e1.printStackTrace(); }
					
					//Envoi des données à la centrale...
					new Thread(new TimeTrackerClient()).start();
				}
			}
		});
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(timeText).addComponent(img).addComponent(employeeList));
        hGroup.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(dateText).addComponent(cio)); //.addComponent(sync)
        layout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(timeText).addComponent(dateText));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(img)); //.addComponent(sync)
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(employeeList).addComponent(cio));
        layout.setVerticalGroup(vGroup);
        
		//Definition de la fenetre :
        frame.setTitle("Polypointeuse ™");
        frame.setSize(350, 155);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
		frame.setVisible(true);
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		thread = null;
	}

	public void run() {
		while (thread != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
			SetLabelsText();
		}
	}
}
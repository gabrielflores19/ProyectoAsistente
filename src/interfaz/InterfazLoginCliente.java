package interfaz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cliente.Cliente;
import server.Mensaje;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class InterfazLoginCliente {

	private JFrame frame;
	private JTextField textFieldIP;
	private JTextField textFieldUsuario;
	private JButton btnConectar;
	private JButton btnIniciarSesion;
	private JButton btnRegistrar;
	private Cliente cliente;
	private JPasswordField textFieldContrasenia;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazLoginCliente window = new InterfazLoginCliente();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public InterfazLoginCliente() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Usuario");
		frame.setResizable(false);
		frame.setBounds(100, 100, 399, 157);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblIp = new JLabel("IP:");
		lblIp.setBounds(10, 23, 46, 14);
		frame.getContentPane().add(lblIp);

		textFieldIP = new JTextField();
		textFieldIP.setText("localhost");
		textFieldIP.setBounds(89, 20, 134, 20);
		frame.getContentPane().add(textFieldIP);
		textFieldIP.setColumns(10);

		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// CONECTAR AL SV Y APAGAR BOTON SI PUDE CONECTARME
				// HABILITO INICIAR SESION SI PUDE CONECTARME AL SV
				String ip = textFieldIP.getText();
				try {
					cliente = new Cliente(ip, 10001);
					cliente.start();
					btnConectar.setEnabled(false);
					btnIniciarSesion.setEnabled(true);
					textFieldUsuario.setEnabled(true);
					textFieldContrasenia.setEnabled(true);
					btnRegistrar.setEnabled(true);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				// btnIniciarSesion.setEnabled(true);
			}
		});
		btnConectar.setBounds(262, 19, 111, 23);
		frame.getContentPane().add(btnConectar);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(10, 54, 88, 14);
		frame.getContentPane().add(lblUsuario);

		textFieldUsuario = new JTextField();
		textFieldUsuario.setEnabled(false);
		textFieldUsuario.setBounds(89, 51, 134, 20);
		frame.getContentPane().add(textFieldUsuario);
		textFieldUsuario.setColumns(10);

		btnIniciarSesion = new JButton("Iniciar sesion");
		btnIniciarSesion.setBounds(262, 50, 111, 23);
		frame.getContentPane().add(btnIniciarSesion);
		// btnIniciarSesion.setEnabled(false);
		btnIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mensaje msj = new Mensaje();
				if (textFieldUsuario.getText().isEmpty() || textFieldContrasenia.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Datos incompletos");
				} else {
					String nombreUsuario = textFieldUsuario.getText();
					String contrasenia = textFieldContrasenia.getText();
					msj.setOrigen(nombreUsuario);
					msj.setContenido(nombreUsuario +"&"+ contrasenia);
					cliente.estado = Cliente.ESPERANDO_LOGIN;
					cliente.enviar(msj);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
					}
					System.out.println("Cliente estado:" + cliente.estado);
					if (cliente.estado == Cliente.LOGGEADO) {
						InterfazSalas principal = new InterfazSalas(cliente, nombreUsuario);
						principal.start();
						frame.dispose();
						JOptionPane.showMessageDialog(null, "Conectado al servidor", "",
								JOptionPane.INFORMATION_MESSAGE);
					} else if (cliente.estado == Cliente.USUARIO_EN_USO) {
						JOptionPane.showMessageDialog(null, "El usuario ya está en uso.", "",
								JOptionPane.INFORMATION_MESSAGE);
					} else if (cliente.estado == Cliente.USUARIO_INVALIDO) {
						JOptionPane.showMessageDialog(null, "Credenciales inválidas.", "",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		btnIniciarSesion.setEnabled(false);

		JLabel lblContrasenia = new JLabel("Contraseña:");
		lblContrasenia.setBounds(10, 84, 88, 14);
		frame.getContentPane().add(lblContrasenia);

		textFieldContrasenia = new JPasswordField();
		textFieldContrasenia.setEnabled(false);
		textFieldContrasenia.setColumns(10);
		textFieldContrasenia.setBounds(89, 80, 134, 20);
		frame.getContentPane().add(textFieldContrasenia);

		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setEnabled(false);
		btnRegistrar.setBounds(262, 79, 111, 23);
		btnRegistrar.addActionListener(e -> abrirRegistro());
		frame.getContentPane().add(btnRegistrar);
	}

	private void abrirRegistro() {
		InterfazRegistro registro = new InterfazRegistro(cliente);
		registro.setVisible(true);
	}
}

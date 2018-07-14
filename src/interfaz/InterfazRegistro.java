package interfaz;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import cliente.Cliente;
import dataAccess.DAUsuario;
import modelos.Usuario;
import server.Mensaje;

public class InterfazRegistro extends JFrame {

	private static final long serialVersionUID = 6194239582768689856L;
	private JPanel contentPane;
	private JTextFieldAlfabetico nombreTextField;
	private JTextFieldAlfabetico apellidoTextField;
	private JTextField emailtextField;
	private JTextField usuarioTextField;
	private JPasswordField contraseniaTextField;
	private String mensajeError;
	private Cliente cliente;

	public InterfazRegistro(Cliente cliente) {
		setComponentes();
		this.cliente = cliente;
	}

	private void setComponentes() {
		setTitle("Registro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 412, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);

		nombreTextField = new JTextFieldAlfabetico();
		nombreTextField.setBounds(70, 48, 270, 19);
		contentPane.add(nombreTextField);
		// nombreTextField.addKeyListener(e->);
		nombreTextField.setColumns(10);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(70, 29, 70, 15);
		contentPane.add(lblNombre);

		JLabel lblNewLabel = new JLabel("Apellido");
		lblNewLabel.setBounds(70, 82, 70, 15);
		contentPane.add(lblNewLabel);

		apellidoTextField = new JTextFieldAlfabetico();
		apellidoTextField.setBounds(69, 101, 271, 19);
		contentPane.add(apellidoTextField);
		apellidoTextField.setColumns(10);

		JLabel lblUsuario = new JLabel("Email");
		lblUsuario.setBounds(70, 138, 70, 15);
		contentPane.add(lblUsuario);

		emailtextField = new JTextField();
		emailtextField.setBounds(70, 159, 270, 19);
		contentPane.add(emailtextField);
		emailtextField.setColumns(10);

		JLabel lblContrasea = new JLabel("Usuario");
		lblContrasea.setBounds(70, 197, 101, 15);
		contentPane.add(lblContrasea);

		usuarioTextField = new JTextField();
		usuarioTextField.setBounds(70, 215, 270, 19);
		contentPane.add(usuarioTextField);
		usuarioTextField.setColumns(10);

		JLabel lblEmail = new JLabel("Contrase\u00f1a");
		lblEmail.setBounds(70, 257, 101, 15);
		contentPane.add(lblEmail);

		contraseniaTextField = new JPasswordField();
		contraseniaTextField.setBounds(70, 278, 270, 19);
		contentPane.add(contraseniaTextField);
		contraseniaTextField.setColumns(10);

		JButton aceptarBtn = new JButton("Aceptar");
		aceptarBtn.setBounds(70, 350, 117, 25);
		aceptarBtn.addActionListener(e -> guardarUsuario());
		contentPane.add(aceptarBtn);

		JButton cancelarBtn = new JButton("Cancelar");
		cancelarBtn.setBounds(223, 350, 117, 25);
		cancelarBtn.addActionListener(e -> this.dispose());
		contentPane.add(cancelarBtn);
	}
	
	private void guardarUsuario() {
		if(validarDatos()){
			String mensaje = usuarioTextField.getText() +"&"+ nombreTextField.getText() +"&"+ apellidoTextField.getText() +"&"+
					emailtextField.getText() +"&"+ contraseniaTextField.getText();
			enviarMensaje(mensaje, Mensaje.REGISTRO);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) { 
			}
			if (cliente.estado == Mensaje.USUARIO_EXISTENTE) {
				JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.");
			} else {
				this.dispose();
			}
		} else {
			JOptionPane.showMessageDialog(this, mensajeError);
		}
	}
	
	private void enviarMensaje(String mensajeNuevo, int tipo) {
		Mensaje m = new Mensaje();
		m.setContenido(mensajeNuevo);
		m.setOrigen(null);
		m.setDestino(null);
		m.setTipo(tipo);				
		cliente.enviar(m);
	}
	
	private boolean validarDatos() {
		if ((nombreTextField.getText().isEmpty() || apellidoTextField.getText().isEmpty() || emailtextField.getText().isEmpty() ||
				usuarioTextField.getText().isEmpty() || contraseniaTextField.getText().isEmpty())){
			mensajeError = "Faltan completar datos.";
			return false;
		}
		return true;
	}
}

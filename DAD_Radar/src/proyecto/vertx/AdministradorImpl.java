package proyecto.vertx;

public class AdministradorImpl {
	
	protected Integer IdAdministrador;
	protected String Username;
	protected String Password;
	
	public AdministradorImpl(Integer IdAdministrador, String Username, String Password) {
		super();
		this.IdAdministrador = IdAdministrador;
		this.Username = Username;
		this.Password = Password;
		
	}

	public AdministradorImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Integer getIdAdministrador() {
		return IdAdministrador;
	}

	public void setIdAdministrador(Integer IdAdministrador) {
		this.IdAdministrador = IdAdministrador;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String Username) {
		this.Username = Username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String Password) {
		this.Password = Password;
	}


	@Override
	public String toString() {
		return "administradorImpl [id=" + IdAdministrador + ", Username=" + Username + ", Password=" + Password + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IdAdministrador == null) ? 0 : IdAdministrador.hashCode());
		result = prime * result + ((Username == null) ? 0 : Username.hashCode());
		result = prime * result + ((Password == null) ? 0 : Password.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdministradorImpl other = (AdministradorImpl) obj;
		if (IdAdministrador == null) {
			if (other.IdAdministrador != null)
				return false;
		} else if (!IdAdministrador.equals(other.IdAdministrador))
			return false;
		if (Username == null) {
			if (other.Username != null)
				return false;
		} else if (!Username.equals(other.Username))
			return false;
		if (Password == null) {
			if (other.Password != null)
				return false;
		} else if (!Password.equals(other.Password))
			return false;
		
		return true;
	}


}

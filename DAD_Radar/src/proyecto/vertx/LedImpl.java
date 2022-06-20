package proyecto.vertx;

public class LedImpl {
	
	protected Integer IdLed;
	protected String Nombre;
	protected Integer Estado;
	
	public LedImpl(Integer IdLed, String Nombre, Integer Estado) {
		super();
		this.IdLed = IdLed;
		this.Nombre = Nombre;
		this.Estado = Estado;
		
	}

	public LedImpl() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Integer getIdLed() {
		return IdLed;
	}

	public void setIdLed(Integer IdLed) {
		this.IdLed = IdLed;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String Nombre) {
		this.Nombre = Nombre;
	}

	public Integer getEstado() {
		return Estado;
	}

	public void setEstado(Integer Estado) {
		this.Estado = Estado;
	}


	@Override
	public String toString() {
		return "ledImpl [id=" + IdLed + ", Nombre=" + Nombre + ", Estado=" + Estado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IdLed == null) ? 0 : IdLed.hashCode());
		result = prime * result + ((Nombre == null) ? 0 : Nombre.hashCode());
		result = prime * result + ((Estado == null) ? 0 : Estado.hashCode());
		
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
		LedImpl other = (LedImpl) obj;
		if (IdLed == null) {
			if (other.IdLed != null)
				return false;
		} else if (!IdLed.equals(other.IdLed))
			return false;
		if (Nombre == null) {
			if (other.Nombre != null)
				return false;
		} else if (!Nombre.equals(other.Nombre))
			return false;
		if (Estado == null) {
			if (other.Estado != null)
				return false;
		} else if (!Estado.equals(other.Estado))
			return false;
		
		return true;
	}

}

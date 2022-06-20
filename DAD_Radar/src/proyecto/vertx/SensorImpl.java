package proyecto.vertx;

public class SensorImpl {
	
	protected Integer IdSensor;
	protected String Nombre;
	protected Integer Estado;
	
	public SensorImpl(Integer IdSensor, String Nombre, Integer Estado) {
		super();
		this.IdSensor = IdSensor;
		this.Nombre = Nombre;
		this.Estado = Estado;
		
	}
	
	public SensorImpl( Integer Estado) {
		
		super();
		this.Estado = Estado;
		
	}

	
	

	public Integer getIdSensor() {
		return IdSensor;
	}

	public void setIdSensor(Integer IdSensor) {
		this.IdSensor = IdSensor;
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
		return "sensorImpl [Estado=" + Estado + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IdSensor == null) ? 0 : IdSensor.hashCode());
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
		SensorImpl other = (SensorImpl) obj;
		if (IdSensor == null) {
			if (other.IdSensor != null)
				return false;
		} else if (!IdSensor.equals(other.IdSensor))
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

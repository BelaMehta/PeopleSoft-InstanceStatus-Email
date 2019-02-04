
public class ExlData {
	
	private String url, username, password, appType, client;
	private String result, toEmail, dbType, ccEmail;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url.trim();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType.trim();
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client.trim();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getEmail() {
		return toEmail;
	}

	public void setEmail(String email) {
		this.toEmail = email.trim();
	}
	
	public String getCCEmail() {
		return ccEmail;
	}

	public void setCCEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType.trim();
	}
	

}

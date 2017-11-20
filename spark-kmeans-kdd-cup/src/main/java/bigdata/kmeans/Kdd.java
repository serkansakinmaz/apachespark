package bigdata.kmeans;

import java.io.Serializable;

public class Kdd implements Serializable {
	private String duration;
	private String protocol_type;
	private String service;
	private String flag;
	private String src_bytes;
	private String dst_bytes;
	private String land;
	private String wrong_fragment;
	private String urgent;
	private String hot;
	private String num_failed_logins;
	private String logged_in;
	private String num_compromised;
	private String root_shell;
	private String su_attempted;
	private String num_root;
	private String num_file_creations;
	private String num_shells;
	private String num_access_files;
	private String num_outbound_cmds;
	private String is_host_login;
	private String is_guest_login;
	private String count;
	private String srv_count;
	private String serror_rate;
	private String srv_serror_rate;
	private String rerror_rate;
	private String srv_rerror_rate;
	private String same_srv_rate;
	private String diff_srv_rate;
	private String srv_diff_host_rate;
	private String dst_host_count;
	private String dst_host_srv_count;
	private String dst_host_same_srv_rate;
	private String dst_host_diff_srv_rate;
	private String dst_host_same_src_port_rate;
	private String dst_host_srv_diff_host_rate;
	private String dst_host_serror_rate;
	private String dst_host_srv_serror_rate;
	private String dst_host_rerror_rate;
	private String dst_host_srv_rerror_rate;
	private String status;
}
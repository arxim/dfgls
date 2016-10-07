package df.bean.obj.util;
/**
 * this class read data file from config on server path 
 * and file read .txt data file 
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFile {

	public String basePath = "C:/data/files";
	public final String DBPath = this.basePath + "/db";
	public final String sqlPath = this.basePath + "/sql";
	public final String logPath = this.basePath + "/log";
	public final String charset = "TIS-620";
	
	/**
	 * create file table
	 * @param table name
	 * @param fields Ex. new String[]{"f1", "f2", "f3"}
	 * @return boolean create success
	 */
	public boolean createTable(String tableName, String[] fields) {
		boolean success = true;
		BufferedWriter output = null;
		try {
			File fPath = new File(this.DBPath);
			if (!fPath.exists()) {
				fPath.mkdirs();
			}
			fPath = new File(this.sqlPath);
			if (!fPath.exists()) {
				fPath.mkdirs();
			}
			fPath = new File(this.logPath);
			if (!fPath.exists()) {
				fPath.mkdirs();
			}
			File table = new File(this.DBPath + File.separator + tableName + ".txt");
			output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(table), this.charset));
			StringBuffer line = new StringBuffer();
			for (int i = 0; i < fields.length; i++) {
				if (i == 0) {
					line.append("#" + fields[i]);
				} else {
					line.append("," + fields[i]);
				}
			}
			output.write(line.toString());
			output.flush();
			output.close();
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * insert file table data
	 * @param table name
	 * @param Map<String[column name], String[column value]> insert data
	 * @return boolean insert success
	 */
	public boolean insertData(String tableName, Map insert) {
		boolean success = true;
		BufferedWriter output = null;
		try {
			File table = new File(this.DBPath + File.separator + tableName + ".txt");
			if (table.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(table));
				String data = null;
				StringBuffer line = new StringBuffer();
				String[] fields = null;
				while ((data = reader.readLine()) != null) {
					if (data.startsWith("#")) {
						fields = data.substring(1, data.length()).split(",");
					}
					line.append(data + "\n");
				}
				if (fields.length > 0) {
					for (int i = 0; i < fields.length; i++) {
						if (!this.chkMap(insert.get(fields[i])).equals("")) {
							if (i == 0) {
								line.append(this.encodeUTF(insert.get(fields[i]).toString()));
							} else {
								line.append("#,#" + this.encodeUTF(insert.get(fields[i]).toString()));
							}
						} else {
							success = false;
						}
					}
				} else {
					success = false;
				}
				reader.close();
				output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(table), this.charset));
				output.write(line.toString());
				output.flush();
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * update file table data
	 * @param table name
	 * @param Map<String[column name], String[column value]> set data
	 * @param Map<String[column name], String[column value]> where data
	 * @return rows update success
	 */
	public int updateData(String tableName, Map set, Map where) {
		int success = 0;
		BufferedWriter output = null;
		try {
			File table = new File(this.DBPath + File.separator + tableName + ".txt");
			if (table.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(table));
				String data = null;
				StringBuffer line = new StringBuffer();
				String[] fields = null;
				boolean foundUpdate = false;
				while ((data = reader.readLine()) != null) {
					if (data.startsWith("#")) {
						fields = data.substring(1, data.length()).split(",");
					} else {
						String[] arrData = data.split("#,#");
						int countWhere = 0;
						if (arrData.length == fields.length) {
							for (int i = 0; i < fields.length; i++) {
								if (this.chkMap(where.get(fields[i])).equals(arrData[i])) {
									countWhere++;
								}
							}
							if (countWhere == where.size()) {
								foundUpdate = true;
								data = "";
								for (int j = 0; j < fields.length; j++) {
									String update = arrData[j];
									if (!this.chkMap(set.get(fields[j])).equals("")) {
										update = this.encodeUTF(this.chkMap(set.get(fields[j])));
									}
									if (j == 0) {
										data += update;
									} else {
										data += "#,#" + update;
									}
									success++;
								}
							}
						}
					}
					line.append(data + "\n");
				}
				reader.close();
				if (foundUpdate) {
					output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(table), this.charset));
					output.write(line.toString());
					output.flush();
					output.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * delete file table data
	 * @param table name
	 * @param Map<String[column name], String[column value]> where data
	 * @return rows delete success
	 */
	public int deleteData(String tableName, Map where) {
		int success = 0;
		BufferedWriter output = null;
		try {
			File table = new File(this.DBPath + File.separator + tableName + ".txt");
			if (table.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(table));
				String data = null;
				StringBuffer line = new StringBuffer();
				String[] fields = null;
				boolean foundDelete = false;
				while ((data = reader.readLine()) != null) {
					boolean delete = false;
					if (data.startsWith("#")) {
						fields = data.substring(1, data.length()).split(",");
					} else {
						String[] arrData = data.split("#,#");
						int countWhere = 0;
						if (arrData.length == fields.length) {
							for (int i = 0; i < fields.length; i++) {
								if (!this.chkMap(where.get(fields[i])).equals("") && arrData[i].startsWith(this.encodeUTF(this.chkMap(where.get(fields[i]))))) {
									countWhere++;
								}
							}
							if (countWhere == where.size()) {
								foundDelete = true;
								delete = true;
								success++;
							}
						}
					}
					if (!delete) {
						line.append(data + "\n");
					}
				}
				reader.close();
				if (foundDelete) {
					output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(table), this.charset));
					output.write(line.toString());
					output.flush();
					output.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	/**
	 * list select file table data
	 * @param table name
	 * @param Map<String[column name], String[column value]> where data
	 * @return List[Map<String[column name], String[column value]>] table data
	 */
	public List listData(String tableName, Map where) {
		List<Map> lsData = new ArrayList<Map>();
		try {
			File table = new File(this.DBPath + File.separator + tableName + ".txt");
			if (table.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(table));
				String data = null;
				String[] fields = null;
				while ((data = reader.readLine()) != null) {
					if (data.startsWith("#")) {
						fields = data.substring(1, data.length()).split(",");
					} else {
						String[] arrData = data.split("#,#");
						int countWhere = 0;
						if (arrData.length == fields.length) {
							if (where != null) {
								for (int i = 0; i < fields.length; i++) {
									if (!this.chkMap(where.get(fields[i])).equals("") && arrData[i].startsWith(this.encodeUTF(this.chkMap(where.get(fields[i]))))) {
										countWhere++;
									}
								}
								if (countWhere == where.size()) {
									Map<String, String> mapData = new HashMap<String, String>();
									for (int j = 0; j < fields.length; j++) {
										mapData.put(fields[j], arrData[j]);
									}
									lsData.add(mapData);
								}
							} else {
								Map<String, String> mapData = new HashMap<String, String>();
								for (int j = 0; j < fields.length; j++) {
									mapData.put(fields[j], arrData[j]);
								}
								lsData.add(mapData);
							}
						}
					}
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lsData;
	}
	/**
	 * get select file table data 1 row only
	 * @param table name
	 * @param Map<String[column name], String[column value]> where data
	 * @return Map<String[column name], String[column value]> table data
	 */
	public Map getData(String tableName, Map where) {
		Map<String, String> mapData = new HashMap<String, String>();
		try {
			File table = new File(this.DBPath + File.separator + tableName + ".txt");
				if (table.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(table));
				String data = null;
				String[] fields = null;
				while ((data = reader.readLine()) != null) {
					if (data.startsWith("#")) {
						fields = data.substring(1, data.length()).split(",");
					} else {
						String[] arrData = data.split("#,#");
						int countWhere = 0;
						if (arrData.length == fields.length) {
							for (int i = 0; i < fields.length; i++) {
								if (!this.chkMap(where.get(fields[i])).equals("") && arrData[i].startsWith(this.encodeUTF(this.chkMap(where.get(fields[i]))))) {
									countWhere++;
								}
							}
							if (countWhere == where.size()) {
								for (int j = 0; j < fields.length; j++) {
									mapData.put(fields[j], arrData[j]);
								}
								break;
							}
						}
					}
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapData;
	}
	
	public String generateCode(String type) {
		String code = "";
		try {
			String tableName = "";
			if (type.equals("db")) {
				tableName = "database";
			} else if (type.equals("cf")) {
				tableName = "convert_function";
			} else if (type.equals("tq")) {
				tableName = "template_query";
			} else if (type.equals("xd")) {
				tableName = "export_data";
			} else if (type.equals("xq")) {
				tableName = "execute_query";
			}
			List ls = this.listData(tableName, null);
			if (ls.size() > 0) {
				Map data = (Map) ls.get((ls.size() -1));
				String lastCode = this.chkMap(data.get(type + "_code"));
				NumberFormat formatter = new DecimalFormat("000");
				code = type + formatter.format((Integer.parseInt(lastCode.substring(2, 5)) + 1));
			} else {
				code = type + "001";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}
	
	private String encodeUTF(String str) throws Exception {
        String output = "";
        if (str != null) {
            try {
                output = new String(str.getBytes("ISO8859_1"), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return output;
    }
	
	public String chkMap(Object obj) {
		String data = "";
		try {
			if (obj != null) {
				data = obj.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
}

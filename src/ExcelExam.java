import java.util.ArrayList;

public class ExcelExam {

	public static void main(String[] args) {
		ExcelExam excel = new ExcelExam();
		ArrayList<double[]> table = new ArrayList<double[]>();
		double[] a = {0, 10, 0.7};
		double[] b = {11, 20, 0.8};
		double[] c = {21, 25, 0.9};
		double[] d = {26, 40, 0.95};
		table.add(a);
		table.add(b);
		table.add(c);
		table.add(d);
//		double[] number = {5, 7, 10, 4, 30};
		double[] number = {50, 20};
//		double[] number = {5, 30};
		ArrayList<Double> ans = excel.calculate(number, table);
		for (int i = 0; i < ans.size(); i++) {
			System.out.println(ans.get(i));
		}
	}
	
	public ArrayList<Double> calculate(double[] number, ArrayList<double[]> table) {
		int index = 0;
		double cumulative = 0;
		ArrayList<Double> summary = new ArrayList<Double>();
		for (int j = 0; j < number.length; j ++) {
			for (int i = 0; i < table.size(); i++) {
				if (cumulative < table.get(i)[1] && index == -999999999) {
					index = i;
				}
			}
			for (int i = 0; i < table.size(); i++) {
				if (cumulative > table.get(table.size() - 1)[1]) {
					summary.add(number[j] * table.get(table.size() - 1)[2]);
					cumulative += number[j];
					break;
				}
				if (number[j] <= table.get(i)[1] && (cumulative + number[j]) <= table.get(i)[1]) {
					if (index == i) {
						summary.add(number[j] * table.get(i)[2]);
						cumulative += number[j];
					} else {
						double tmp2 = number[j], tmp3;
						ArrayList<Double> collect = new ArrayList<Double>();
						for(int k = index; k <= i; k++) {
							double tmp = table.get(k)[1] - cumulative;
							tmp3 = tmp2;
							tmp2 -= tmp;
							if (tmp2 >= 0) {
								collect.add(tmp * table.get(k)[2]);
								cumulative += tmp;
							} else {
								collect.add(tmp3 * table.get(k)[2]);
								cumulative += tmp3;
							}
						}
						double tmp4 = 0;
						for (int p = 0; p < collect.size(); p++) {
							tmp4 += collect.get(p);
						}
						summary.add(tmp4);
					}
					break;
				} else if (number[j] <= table.get(i)[1] && (cumulative + number[j]) > table.get(i)[1]) {
					continue;
				} else if (number[j] >= table.get(table.size() - 1)[1]) {
					if (cumulative > table.get(table.size() - 2)[1]) {
						summary.add(number[j] * table.get(table.size() - 1)[2]);
						cumulative += number[j];
					} else {
						index = -999999999;
						for (int l = 0; l < table.size(); l++) {
							if (cumulative < table.get(l)[1] && index == -999999999) {
								index = l;
							}
						}
						double tmp2 = number[j], tmp3;
						ArrayList<Double> collect = new ArrayList<Double>();
						for(int k = index; k <= table.size() - 1; k++) {
							double tmp = table.get(k)[1] - cumulative;
							tmp3 = tmp2;
							tmp2 -= tmp;
							if (tmp2 >= 0) {
								if (k == table.size() - 1) {
									collect.add(tmp3 * table.get(k)[2]);
									cumulative += tmp3;
								} else {
									collect.add(tmp * table.get(k)[2]);
									cumulative += tmp;
								}
							} else {
								summary.add(tmp3 * table.get(k)[2]);
								cumulative += tmp3;
							}
						}
						double tmp4 = 0;
						for (int p = 0; p < collect.size(); p++) {
							tmp4 += collect.get(p);
						}
						summary.add(tmp4);
					}
					break;
				}
			}
			index = -999999999;
		}
		return summary;
	}
}
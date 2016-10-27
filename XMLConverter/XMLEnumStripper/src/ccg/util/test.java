package ccg.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class test {

	public static void main(String[] args) throws IOException {
		OLI_LU_STATE_helper helper = new OLI_LU_STATE_helper();
//		Map<String, List<String>> map = helper.loadData();
//		
//		Set<String> set = map.keySet();
//		for (String s : set) {
//			System.out.print(s + ": ");
//			for (String value : map.get(s)){
//				System.out.print(value + ", ");
//			}
//			System.out.println();
//		}
		List<String> context = new ArrayList<String>();

		
		context = helper.loadContext("OLI_LU_STATE");
		System.out.println(context);
	}

}

import java.util.Stack;
import java.util.ArrayList;

public class Criba{

	Stack <Integer> getNum(int a){
		int i, j, array01[] = new int[a];
		ArrayList<Integer> array00 = new ArrayList<Integer>();
		Stack <Integer> stack = new Stack <Integer> ();
		
		for(i = 0; i <= a-2; i++){
			array01[i] = i+2;
		}
		
		for(i = 0; i <= a-2; i++){
			if(array00.contains(array01[i])){
				continue;
			}
			else{
				for(j = i+1; j <= a-2; j++){
					if(!array00.contains(array01[i]) && array01[j] % array01[i] == 0){
						array00.add(array01[j]);
					}
					else{continue;}
				}
			}
		}
		
		for(i = 0; i <= a-2; i++){
			if(!array00.contains(array01[i])){stack.push(array01[i]);}
			else{continue;}
		}

		return stack;
	}


}
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class DoomsdayFuel {
	public static void main(String[] args) {
		int[][] t1 = {{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};
		int[] r1 = solution(t1);
		StringJoiner sj = new StringJoiner(", ");
		for (int y = 0; y < r1.length; y++) {
			sj.add(String.valueOf(r1[y]));
		}
		System.out.println("[ " + sj.toString() + " ]");
//		int[][] t2 = {{0, 2, 1, 0, 0}, {0, 0, 0, 3, 4}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
//		System.out.print(solution(t2));
	}
	
	public static final DoomsdayFuel a = new DoomsdayFuel();
	public static final Fraction ONE = a.new Fraction(1,1);
	public static final Fraction NEGATIVE_ONE = a.new Fraction(-1,1);
	public static final Fraction ZERO = a.new Fraction(0,1);

	public static int[] solution(int[][] xs) {
		// set up some constants to use
		
		final int size = xs.length;

		//		printMatrix(xs);

		// hold some information to make setting up the matrices easier
		Map<Integer, int[]> nonabsorbing = new HashMap<Integer, int[]>();
		List<Integer> amcMap = new ArrayList<Integer>();

		// find all absorbing/nonabsorbing states
		for (int pos = 0; pos < size; pos++) {
			if (absorbing(pos, xs[pos])) {
				amcMap.add(pos);
			} else {
				nonabsorbing.put(pos, xs[pos]);
			}
		}
		amcMap.addAll(nonabsorbing.keySet());
		int absorbingSize = size - nonabsorbing.size();

		// set-up the identity matrix
		Fraction[][] i = new Fraction[absorbingSize][absorbingSize];
		for (int pos = 0; pos < size - nonabsorbing.size(); pos++) {
			i[pos][pos] = ONE;
		}

		// create the r and q matrices using the input values
		Fraction[][] r = new Fraction[nonabsorbing.size()][absorbingSize];
		Fraction[][] q = new Fraction[nonabsorbing.size()][nonabsorbing.size()];
		for (int x = 0; x < nonabsorbing.size(); x++) {
			int stateX = amcMap.get(x + absorbingSize);
			int[] outs = nonabsorbing.get(stateX);
			int denom = denominator(outs);
			int y = 0;
			for (; y < absorbingSize; y++) {
				int stateY = amcMap.get(y);
				r[x][y] = a.new Fraction(outs[stateY], denom).simplify();
			}
			for (; y < size; y++) {
				int stateY = amcMap.get(y);
				q[x][y-absorbingSize] = a.new Fraction(outs[stateY], denom).simplify();
			}
		}

		// calculate the I-Q matrix
		Fraction[][] imq = new Fraction[nonabsorbing.size()][nonabsorbing.size()];
		for (int x = 0; x < imq.length; x++) {
			for (int y = 0; y < imq[x].length; y++) {
				imq[x][y] = q[x][y].subtractFrom(i[x][y]);
			}
		}
		
		Fraction[][] f = inverse(imq);
		
		Fraction[][] fr = multiply(f, r);
		
		int lcd = ONE.leastCommonDivisor(fr[0]);
		int[] res = new int[fr[0].length + 1];
		res[fr[0].length] = lcd;
		
		for (int x = 0; x < fr[0].length; x++) {
			if (fr[0][x].denom == lcd) {
				res[x] = fr[0][x].num;
			} else {
				res[x] = fr[0][x].num * (lcd/fr[0][x].denom);
			}
		}
		
		return res;
	}
	
	// basic test functions
	public static boolean absorbing(int pos, int[] test) {
		for (int x = 0; x < test.length; x++) {
			if (test[x] > 0 && x != pos) {
				return false;
			}
		}
		return true; 
	}

	public static int denominator(int[] states) {
		int sum = 0;
		for (int x : states) {
			sum += x;
		}
		return sum;
	}
	
	// functions for inversing a matrix of Fractions
	public static Fraction[][] newMatrixOfSameSize(Fraction[][] original) {
		return new Fraction[original.length][original[0].length];
	}
	
	public static Fraction negIfEven(int i) {
		return i % 2 == 0 ? NEGATIVE_ONE : ONE;
	}

	public static Fraction[][] transpose(Fraction[][] matrix) {
		Fraction[][] transposedMatrix = newMatrixOfSameSize(matrix);
		for (int x = 0; x < matrix.length; x++) {
			for (int y = 0; y < matrix[0].length; y++) {
				transposedMatrix[y][x] = matrix[x][y];
			}
		}
		return transposedMatrix;
	}
	
	public static Fraction[][] createSubMatrix(Fraction[][] matrix, int excluding_row, int excluding_col) {
		Fraction[][] mat = new Fraction[matrix.length-1][matrix[0].length-1];// new Matrix(matrix.getNrows()-1, matrix.getNcols()-1);
	    int r = -1;
	    for (int x=0;x<matrix.length;x++) {
	        if (x==excluding_row)
	            continue;
	            r++;
	            int c = -1;
	        for (int y=0;y<matrix[0].length;y++) {
	            if (y==excluding_col)
	                continue;
	            mat[r][++c] = matrix[x][y];
	        }
	    }
	    return mat;
	}

	public static Fraction determinant(Fraction[][] matrix){
		if (matrix.length == 1 && matrix[0].length == 1) {
			return matrix[0][0];
		}
		if (matrix.length == 2 && matrix[0].length == 2) {
			return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0])).simplify();
		}
		Fraction sum = ZERO;
		for (int x = 0; x < matrix[0].length; x++) {
			sum = sum.add(matrix[0][x].multiply(determinant(createSubMatrix(matrix, 0, x))).multiply(negIfEven(x)));
		}
		return sum;
	}
	
	public static Fraction[][] cofactor(Fraction[][] matrix) {
		Fraction[][] mat = newMatrixOfSameSize(matrix);
	    for (int x = 0; x < matrix.length; x++) {
	        for (int y = 0; y < matrix[0].length; y++) {
	            mat[x][y] = negIfEven(x).multiply(negIfEven(y)).multiply(determinant(createSubMatrix(matrix, x, y)));
	        }
	    }
	    return mat;
	}
	
	public static Fraction[][] inverse(Fraction[][] matrix) {
	    Fraction[][] mat = transpose(cofactor(matrix));
	    Fraction c = ONE.divide(determinant(matrix));
	    for (int x = 0; x < mat.length; x++) {
	    	for (int y = 0; y < mat[0].length; y++) {
	    		mat[x][y] = mat[x][y].multiply(c).simplify();
	    	}
	    }
	    return mat;
	}
	
	public static Fraction[][] multiply(Fraction[][] firstMatrix, Fraction[][] secondMatrix) {
        Fraction[][] product = new Fraction[firstMatrix.length][secondMatrix[0].length];
        for(int x = 0; x < firstMatrix.length; x++) {
            for (int y = 0; y < secondMatrix[0].length; y++) {
                for (int z = 0; z < firstMatrix[0].length; z++) {
                    product[x][y] = firstMatrix[x][z].multiply(secondMatrix[z][y]).add(product[x][y]).simplify();
                }
            }
        }
        return product;
    }
	
	// DIY Fraction class w/ Euclidean simplification
	public class Fraction {
		private int num, denom;

		public Fraction(int num, int denom) {
			this.denom = denom;
			this.num = num;
		}

		public String toString() {
			if (denom == 1) {
				return String.valueOf(num);
			} else {
				return num + "/" + denom;
			}
		}

		public Fraction subtract(Fraction sub) {
			if (sub == null) sub = new Fraction(0,1);
			return new Fraction((this.num * sub.denom)-(this.denom * sub.num), this.denom * sub.denom);
		}

		public Fraction subtractFrom(Fraction sub) {
			if (sub == null) sub = new Fraction(0,1);
			return new Fraction((this.denom * sub.num)-(this.num * sub.denom), this.denom * sub.denom);
		}

		public Fraction add(Fraction add) {
			if (add == null) add = new Fraction(0,1);
			return new Fraction((this.num * add.denom)+(this.denom * add.num), this.denom * add.denom);
		}

		public Fraction flip() {
			return new Fraction(this.denom, this.num);
		}

		public Fraction multiply(Fraction mult) {
			if (mult == null) return new Fraction(0,1);
			return new Fraction(this.num * mult.num, this.denom * mult.denom);
		}
		
		public Fraction divide(Fraction div) {
			if (div == null) throw new ArithmeticException("Cannot divide " + this.toString() + " by zero!");
			return new Fraction(this.num * div.denom, this.denom * div.num);
		}

		public Fraction simplify() {
			int g = this.gcd(num, denom);
			return new Fraction(num/g, denom/g);
		}

		private int gcd(int n1, int n2) {
			if (n2 == 0) {
				return n1;
			}
			return gcd(n2, n1 % n2);
		}
		
		public int leastCommonDivisor(Fraction... fs) 
	    { 
			int[] denoms = new int[fs.length];
			for (int x = 0; x < fs.length; x++)
				denoms[x] = fs[x].denom;
			
			return Arrays.stream(denoms).reduce(1, (x, y) -> x * (y / gcd(x, y)));
	    } 
	}
	
	// printing stuff for debug
//	public static void printMatrix(Object[][] m) {
//		for (int x = 0; x < m.length; x++) {
//			StringJoiner sj = new StringJoiner(", ");
//			for (int y = 0; y < m[x].length; y++) {
//				sj.add(String.valueOf(m[x][y]));
//			}
//			System.out.println("[ " + sj.toString() + " ]");
//		}
//	}
//
//	public static void printMatrix(int[][] m) {
//		for (int x = 0; x < m.length; x++) {
//			StringJoiner sj = new StringJoiner(", ");
//			for (int y = 0; y < m[x].length; y++) {
//				sj.add(String.valueOf(m[x][y]));
//			}
//			System.out.println("[ " + sj.toString() + " ]");
//		}
//	}
}

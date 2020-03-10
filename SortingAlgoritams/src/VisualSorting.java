import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VisualSorting {
	
	private JFrame jf;
	//promenjive
	private int len = 50;
	private int off = 0;
	private int curAlg = 0;
	private int spd = 15;
	private int compare = 0;
	private int acc = 0;
	private final int SIZE = 600;
	private int current = -1;
	private int check = -1;
	private int width = SIZE/len;
	private int type = 0;
	
	private int[] array;
	
	private String[] algorithms = {"Cocktail Sort", 
								   "Merge Sort",
								   "Shell Sort",
								   "Tim Sort", 
								   "Heap Sort", 
								   "Counting Sort", 
								   };
	
	
	private boolean sorting = false;
	private boolean shuffled = true;
	
	SortingAlgorithms algorithm = new SortingAlgorithms();
	Random r = new Random();
	
	JPanel panel = new JPanel();
	GraphCanvas canvas;
	JLabel speedL = new JLabel("Speed :");
	JLabel msL = new JLabel(spd+" ms");
	JLabel sizeL = new JLabel("Size :");
	JLabel lenL = new JLabel(len+"");
	JLabel compareL = new JLabel("Comparisons : " + compare);
	JLabel accessL = new JLabel("Array Accesses : " + acc);
	JLabel algorithmL = new JLabel("Algorithms");
	
	JComboBox comboBox = new JComboBox(algorithms);

	JButton sort = new JButton("Sort");
	JButton shuffle = new JButton("Shuffle");
	JSlider size = new JSlider(JSlider.HORIZONTAL,1,6,1);
	JSlider speed = new JSlider(JSlider.HORIZONTAL,0,100,spd);
	
	Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	
	public VisualSorting() {
		shuffleList();	
		initialize();
	}
	
	public void createList() {
		array = new int[len];	
		for(int i = 0; i < len; i++) {
			array[i] = i+1;
		} 
	}
	
	public void shuffleList() {
		createList();
		for(int a = 0; a < 500; a++) {	
			for(int i = 0; i < len; i++) {	
				int rand = r.nextInt(len);	
				int temp = array[i];			
				array[i] = array[rand];		
				array[rand] = temp;			
			}
		}
		sorting = false;
		shuffled = true;
	}
	
	public void initialize() {
		
		jf = new JFrame();
		jf.setSize(800,650);
		jf.setTitle("Sorting Algorithms");
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.getContentPane().setLayout(null);
		
	
		panel.setLayout(null);
		panel.setBounds(5,10,180,400);
		panel.setBorder(BorderFactory.createTitledBorder(loweredetched,"Controls"));
		
		
		algorithmL.setHorizontalAlignment(JLabel.CENTER);
		algorithmL.setBounds(40,20,100,25);
		panel.add(algorithmL);
		
                comboBox.setBounds(30,45,120,25);
		panel.add(comboBox);
		
		sort.setBounds(40,100,100,25);
		panel.add(sort);
		
	        speedL.setHorizontalAlignment(JLabel.LEFT);
		speedL.setBounds(10,130,50,25);
		panel.add(speedL);
		
		
		msL.setHorizontalAlignment(JLabel.LEFT);
		msL.setBounds(135,130,50,25);
		panel.add(msL);
		
		
		speed.setMajorTickSpacing(5);
		speed.setBounds(55,130,75,25);
		speed.setPaintTicks(false);
		panel.add(speed);
		
	        panel.add(shuffle);
		sizeL.setHorizontalAlignment(JLabel.LEFT);
		sizeL.setBounds(10,175,50,25);
		panel.add(sizeL);
		
	
		lenL.setHorizontalAlignment(JLabel.LEFT);
		lenL.setBounds(140,175,50,25);
		panel.add(lenL);
		
		
		size.setMajorTickSpacing(50);
		size.setBounds(55,175,75,25);
		size.setPaintTicks(false);
		panel.add(size);
		
		
		compareL.setHorizontalAlignment(JLabel.LEFT);
		compareL.setBounds(10,220,200,25);
		panel.add(compareL);
		
		
		accessL.setHorizontalAlignment(JLabel.LEFT);
		accessL.setBounds(10,260,200,25);
		panel.add(accessL);
		
		panel.setBackground(Color.YELLOW);
		canvas = new GraphCanvas();
		canvas.setBounds(190,0,SIZE,SIZE);
		canvas.setBorder(BorderFactory.createLineBorder(Color.black));
		jf.getContentPane().add(panel);
		jf.getContentPane().add(canvas);

		//ADD ACTION LISTENERS
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				curAlg = comboBox.getSelectedIndex();
			
			}
			
		});

	shuffle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shuffleList();
				reset();
			}
		});
		sort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(shuffled) {
					sorting = true;
					compare = 0;
					acc = 0;
				}
				
			}
		});
                
           
		speed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				spd = (int)speed.getValue();
				msL.setText(spd+" ms");
			}
		});
                
		size.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int val = size.getValue();
				if(size.getValue() == 5)
					val = 4;
				len = val * 50;
				lenL.setText(len+"");
				if(array.length != len)
					shuffleList();
				reset();
			}
			
		});
                
                sorting();
        
        }

	public void sorting() {
		if(sorting) {
			try {
				switch(curAlg) {	
					case 0:
						algorithm.cocktailSort();
						break;
					case 1:
					       algorithm.mergeSort(0,len-1);
						break;
					case 2:
						
						break;
					case 3:
						algorithm.timSort(len);
						break;
					case 4:
						algorithm.heapSort();
						break;
					case 5:
						algorithm.countSort(0, len-1);
						break;
				
					default:
						algorithm.insertionSort(0, len-1);
						break;
				}
			} catch(IndexOutOfBoundsException e) {}	
		}
		reset();
		pause();	
	}
	
	
	public void pause() {
		int i = 0;
		while(!sorting) {	
			i++;
			if(i > 100)
				i = 0;
			try {
				Thread.sleep(1);
			} catch(Exception e) {}
		}
		sorting();	
	}
	
	
	public void reset() {
		sorting = false;
		current = -1;
		check = -1;
		off = 0;
		Update();
	}
	
	
	public void speed() {
		try {
			Thread.sleep(spd);
		} catch(Exception e) {}
	}
	
	
	public void Update() {	
		width = SIZE/len;
		canvas.repaint();
		compareL.setText("Comparisons : " + compare);
		accessL.setText("Array Accesses : " + acc);
	}
	
	public static void main(String[] args) {
		new VisualSorting();
        }
	class GraphCanvas extends JPanel {
		
		public GraphCanvas() {
			setBackground(Color.black);
		}
		
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for(int i = 0; i < len; i++) {	
				int HEIGHT = array[i]*width;	
				if(type == 0) {		
					g.setColor(Color.white);
					if(current > -1 && i == current) {
						g.setColor(Color.green);	
					}
					if(check > -1 && i == check) {
						g.setColor(Color.red);	
					}
					
					g.fillRect(i*width, SIZE-HEIGHT, width, HEIGHT);
					g.setColor(Color.black);
					g.drawRect(i*width, SIZE-HEIGHT, width, HEIGHT);
				}
				else if(type == 1) {
					g.setColor(Color.white);
					if(current > -1 && i == current) {
						g.setColor(Color.green);	
					}
					if(check > -1 && i == check) {
						g.setColor(Color.red);	
					}
					
					g.fillOval(i*width,SIZE-HEIGHT,width,width);
				}
			}
		}
	}
	
	class SortingAlgorithms {
		
	
		
		public void insertionSort(int start, int end) {
			for(int i = start+1; i <= end; i++) {
				current = i;
				int j = i;
				while(array[j] < array[j-1] && sorting) {
					swap(j,j-1);
					check = j;
					compare++;	acc+=2;
					Update();
					speed();
					if(j > start+1)
						j--;
				}
			}
		}
		
		
		
		public void cocktailSort() {
			boolean noswaps = false;
			int c = 0;
			while(!noswaps && sorting) {
				noswaps = true;
				int i;
				int target;
				int inc;
				if(off == 1) {
					i = len-2-c;
					target = c-1;
					inc = -1;
				} else {
					i = c;
					target = len-2-c;
					inc = 1;
				}
				current = target+1;
				while(i!=target && sorting) {
					if(array[i] > array[i+1]) {
						noswaps = false;
						swap(i,i+1);
					}
					check = i+1-off;
					compare++;	acc+=2;
					Update();
					speed();
					i+=inc;
				}
				if(off == 1)
					c++;
				off = 1- off;
			}
		}
                
                public void shell() {
                    
                    int increment =array.length/2;
                    while (increment>0 && sorting) {
                        for (int i= increment; i<array.length; i++) {
                            int j=i;
                            int temp =array[i];
                            while (j>=increment && array[j-increment] > temp && sorting) {
                                array[j]=array[j-increment];
                                j=j-increment;
                                
                            }
                            array[j] = temp;
                        
                        }
                        Update();
                        speed();
                        if(increment ==2) {
                            increment =1;
                        } else {
                            increment*=(5.0/11);
                        }
                    }
                }
		
		public void heapSort() {
			heapify(len);
			int end = len-1;
			while(end > 0 && sorting) {
				current = end;
				swap(end,0);
				end--;
				siftDown(0,end);
				Update();
				speed();
			}
		}
		
		public void heapify(int n) {
			int start = iParent(n-1);
			while(start >= 0 && sorting) {
				siftDown(start, n-1);
				start--;
				Update();
				speed();
			}
		}
		
		public void siftDown(int start, int end) {
			int root = start;
			while(iLeftChild(root) <= end && sorting) {
				int child = iLeftChild(root);
				int swap = root;
				check = root;
				if(array[swap] < array[child]) {
					swap = child;
				} if(child+1 <= end && array[swap] < array[child+1]) {
					swap = child+1;
				} if(swap == root) {
					return;
				} else {
					swap(root,swap);
					check = root;
					root = swap;
				}
				compare+=3;	acc+=4;
				Update();
				speed();
			}
		}
		
		public int iParent(int i) { return ((i-1)/2); }
		public int iLeftChild(int i) { return 2*i + 1; }
		
		
		
		
		void merge(int l, int m, int r)
	    {
	        int n1 = m - l + 1;
	        int n2 = r - m;
	 
	        int L[] = new int [n1];
	        int R[] = new int [n2];
	 
	        for (int i=0; i<n1; i++) {
	            L[i] = array[l + i];	acc++;
	        }
	        for (int j=0; j<n2; j++) {
	            R[j] = array[m + 1+ j];	acc++;
	        }
	        int i = 0, j = 0;

	        int k = l;
	        while (i < n1 && j < n2 && sorting) {
	        	check = k;
	            if (L[i] <= R[j]) {
	                array[k] = L[i];	acc++;
	                i++;
	            } else {
	                array[k] = R[j];	acc++;
	                j++;
	            }
	            compare++;
	            Update();
	            speed();
	            k++;
	        }

	        while (i < n1 && sorting) {
	            array[k] = L[i];	acc++;
	            i++;
	            k++;
	            Update();
	            speed();
	        }

	        while (j < n2 && sorting) {
	            array[k] = R[j];	acc++;
	            j++;
	            k++;
	            Update();
	            speed();
	        }
	    }

	    public void mergeSort(int l, int r) {
	        if (l < r) {
	            int m = (l+r)/2;
	            current = r;
	            mergeSort(l, m);
	            mergeSort(m+1, r);
	 
	            merge(l, m, r);
	        }
	    }
	    
	
	    
	    
	    
	    public void countSort(int n, int exp) {
	    	int output[] = new int[n];
	    	int i;
	    	int count[] = new int[10];
	    	Arrays.fill(count, 0);
	    	
	    	for(i = 0; i < n; i++)	{
	    		count[(array[i]/exp)%10]++;	acc++;
	    	}
	    	
	    	for(i = 1; i < 10; i++) {
	    		count[i] += count[i - 1];
	    	}
	    	
	    	for(i = n -1; i >= 0; i--) {
	    		output[count[(array[i] / exp) % 10] - 1] = array[i];	acc++;
	    		count[(array[i] / exp) % 10]--;	acc++;
	    	}
	    	for(i = 0; i < n; i++) {
	    		if(!sorting)
	    			break;
	    		check = i;
	    		array[i] = output[i];	acc++;
	    		Update();
	    		speed();
	    	}
	    }
	    
	    public int getMax(int n) {
	    	int mx = array[0];
	    	for(int i = 1; i < n; i++) {
	    		if(array[i] > mx)
	    			mx = array[i];
	    		compare++;	acc++;
	    	}
	    	return mx;
	    }
	    
	    public void timSort(int n) {
	    	int RUN = 64;
	    	if(len >64) {
	    		while(((double)len/RUN)%2!=0)
	    			RUN--;
	    	}
	    	for(int i = 0; i < n; i+=RUN) {
	    		insertionSort(i, Math.min((i+RUN-1), (n-1)));
	    	}
	    	
	    	for(int size = RUN; size < n; size = 2*size) {
	    		for(int left = 0; left < n; left += 2*size) {
	    			int mid = left + size - 1;
	    			int right = Math.min((left + 2*size-1), (n-1));
	    			
	    			merge(left, mid, right);
	    		}
	    		if(!sorting)
					break;
	    	}
	    }
		

		
		public void swap(int i1, int i2) {
			int temp = array[i1];	acc++;
			array[i1] = array[i2];	acc+=2;
			array[i2] = temp;	acc++;
		}
		
		public boolean checkSorted() {
			for(int i = 0; i < len-1; i++) {
				if(array[i] > array[i+1]) {	acc+=2;
					return false;
				}
			}
			return true;
		}
	}
}

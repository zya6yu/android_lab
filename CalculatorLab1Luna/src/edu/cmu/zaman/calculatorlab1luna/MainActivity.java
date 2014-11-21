package edu.cmu.zaman.calculatorlab1luna;

import java.math.BigDecimal;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	// variables
	private BigDecimal number1 = new BigDecimal("0");
	private BigDecimal number2 = new BigDecimal("0");
	private int operation = -1;

	// state variables
	private boolean number1set = false;
	private boolean number2set = false;
	private boolean operationset = false;
	private boolean decimal = false;
	private int decimalTail = 1;
	private boolean number1neg = false;
	private boolean number2neg = false;

	private BigDecimal neg1 = new BigDecimal(-1);

	// output field
	public TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		tv = (TextView) findViewById(R.id.output); // initialize text field
		tv.setText("0");
		
		getActionBar().setTitle("Zaman Calculator");   
		getSupportActionBar().setTitle("Zaman Calculator");

	}

	// resets numbers 1 and 2 to 0 and operation 0, sets text back to 0
	public void clear_calc(View view) {
		System.out.print("clear clicked");

		number1 = BigDecimal.ZERO;
		number2 = BigDecimal.ZERO;
		number1set = false;
		number2set = false;
		operationset = false;
		operation = -1;
		decimal = false;
		decimalTail = 1;
		number1neg = false;
		number2neg = false;

		// set calculator text to 0
		tv.setText("0");
	}

	// evaluate expression based on number1, number2, operation
	// returns the evaluated expression
	public BigDecimal equal_calc(View view) {
		BigDecimal result = new BigDecimal("-9999999");
		boolean valid = true;
		Log.d("Calculator", Integer.toString(operation));
		if (!(number1set && number2set && operation != -1)) { // not valid
			// show toast
			Log.e("Calculator", "not a valid operation");
			valid = false;
		} else { // valid
			result = number1;
			// run switch on operation value
			switch (operation) {
			case 0: // divide
				result = result.divide(number2);
				break;
			case 1: // multiply
				result = result.multiply(number2);
				break;
			case 2: // minus
				result = result.subtract(number2);
				break;
			case 3: // plus
				result = result.add(number2);
				break;
			}
		}
		if(valid == true){
		tv.setText(result.toString());
		number1 = result;
		number2 = BigDecimal.ZERO;
		number1set = true;
		number2set = false;
		operationset = false;
		operation = -1;
		decimal = false;
		decimalTail = 1;
		number1neg = false;
		number2neg = false;
		}
		else{
			Context context = getApplicationContext();
			CharSequence text = "Don't have a valid sequence, missing either first number, operator, or second number";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		return result;
	}

	// the same as equal_calc() but without the view operation -- called from
	// within the application (rather than by an onClick event)
	public BigDecimal do_operation() {
		BigDecimal result = new BigDecimal("-9999999");
		Log.d("Calculator", Integer.toString(operation));
		if (!(number1set && number2set && operation != -1)) { // not valid
			// show toast
			Log.e("Calculator", "not a valid operation");
		} else { // valid
			result = number1;
			// run switch on operation value
			switch (operation) {
			case 0: // divide
				result = result.divide(number2);
				break;
			case 1: // multiply
				result = result.multiply(number2);
				break;
			case 2: // minus
				result = result.subtract(number2);
				break;
			case 3: // plus
				result = result.add(number2);
				break;
			}
		}
		tv.setText(result.toString());
		number1 = result;
		number2 = BigDecimal.ZERO;
		number1set = true;
		number2set = false;
		operationset = false;
		operation = -1;
		decimal = false;
		decimalTail = 1;
		number1neg = false;
		number2neg = false;
		return result;
	}

	// on number button press
	public int number_calc(View v) {
		// get which number was pressed from the tag of the button
		int tag = Integer.parseInt(v.getTag().toString());
		Log.d("calculator", "number button press: " + Integer.toString(tag));

		BigDecimal bigTag = new BigDecimal(tag);

		if (!number1set) { // first number isn't set
			if (decimal == false) {
				number1 = new BigDecimal(tag);
			} else if (decimal == true) {
				number1 = new BigDecimal(tag).divide(BigDecimal.TEN);
				Log.d("calculator", number1.toString());
				decimalTail++;
			}
			number1set = true;
			if (number1neg == true) {
				number1 = number1.multiply(neg1); // make negative
			}
			tv.setText(number1.toString());
		} else if (number1set && !operationset) {
			if (decimal == false) {
				if (number1neg == false) {
					number1 = number1.multiply(BigDecimal.TEN).add(bigTag);
				} else { // number1 is already negative. To add, multiply to
							// positive, add, then multiply back
					number1 = number1.multiply(neg1).multiply(BigDecimal.TEN)
							.add(bigTag).multiply(neg1);
				}

			} else if (decimal == true) {
				if (number1neg == false) {
					number1 = number1.add(bigTag.divide(BigDecimal.TEN
							.pow(decimalTail)));
				} else {
					number1 = number1
							.multiply(neg1)
							.add(bigTag.divide(BigDecimal.TEN.pow(decimalTail)))
							.multiply(neg1);
				}
				decimalTail++;
			}
			tv.setText(number1.toString());
		}

		else if (!number2set && operationset) {
			if (decimal == false) {
				number2 = new BigDecimal(tag);
			} else if (decimal == true) {
				number2 = new BigDecimal(tag).divide(BigDecimal.TEN);
				decimalTail++;
			}
			if (number2neg == true) {
				number2 = number2.multiply(neg1); // make negative
			}
			number2set = true;
			tv.setText(number2.toString());
		} else if (operationset && number2set) {
			if (decimal == false) {
				if (number2neg == false) {
					number2 = number2.multiply(BigDecimal.TEN).add(bigTag);
				} else {
					number2 = number2.multiply(neg1).multiply(BigDecimal.TEN)
							.add(bigTag).multiply(neg1);
				}
			} else if (decimal == true) {
				if (number2neg == false) {
					number2 = number2.add(bigTag.divide(BigDecimal.TEN
							.pow(decimalTail)));
				} else {
					number2 = number2
							.multiply(neg1)
							.add(bigTag.divide(BigDecimal.TEN.pow(decimalTail)))
							.multiply(neg1);
				}

				decimalTail++;
			}
			tv.setText(number2.toString());
		}

		return tag;
	}

	// on operator button press
	public int operator_calc(View v) {
		int tag = Integer.parseInt(v.getTag().toString());
		Log.d("calculator", "operator button press: " + Integer.toString(tag));
		switch (tag) {
		case 0:
			Log.d("calculator", "divide");
			break;
		case 1:
			Log.d("calculator", "multiply");
			break;
		case 2:
			Log.d("calculator", "subtract");
			break;
		case 3:
			Log.d("calculator", "addition");
			break;
		default:
			Log.e("calculator",
					"received operator not in range 0-3, this is invalid");
			break;
		}
		if (number1set == false && tag == 2) { // make number1 negative
			number1neg = true;
		} else if (number1set == true && operationset == false) {
			operation = tag;
			operationset = true;
			decimal = false;
			decimalTail = 1;
		} else if (number1set == true && operationset == true
				&& number2set == false && tag == 2) {
			// make second number negative
			number2neg = true;
		} else if (number1set == true && operationset == true
				&& number2set == false && tag != 2) {
			Log.e("calculator", "already have an operator, displaying toast");
			Context context = getApplicationContext();
			CharSequence text = "Already have an operator, ignoring previously pressed operator";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else if (number1set == true && operationset == true
				&& number2set == true) {
			// already have a full operation (number1 + operator + number2)

			// so evaluate
			do_operation();

			// set next operation
			operation = tag;
			operationset = true;
			decimal = false;
			decimalTail = 1;
			number1neg = false;
			number2neg = false;
		} else {
			Log.e("calculator", "error setting operation");
		}

		return tag;
	}

	// on decimal point press
	public void decimal_calc(View v) {
		decimal = true;
	}

	// on double zero press
	public boolean firstzero = true;

	public int doublezero_calc(View v) {
		// get which number was pressed from the tag of the button
		int tag = 0;
		Log.d("calculator", "number button press: " + Integer.toString(tag));

		BigDecimal bigTag = new BigDecimal(tag);

		if (!number1set) { // first number isn't set
			if (decimal == false) {
				number1 = new BigDecimal(tag);
			} else if (decimal == true) {
				number1 = new BigDecimal(tag).divide(BigDecimal.TEN);
				decimalTail++;
				decimalTail++;
			}
			number1set = true;
			tv.setText(number1.toString());
		} else if (number1set && !operationset) {
			if (decimal == false) {
				number1 = number1.multiply(BigDecimal.TEN).multiply(
						BigDecimal.TEN);
			} else if (decimal == true) {
				number1 = number1.add(bigTag.divide(BigDecimal.TEN
						.pow(decimalTail)));
				decimalTail++;
				decimalTail++;
			}
			tv.setText(number1.toString());
		}

		else if (!number2set && operationset) {
			if (decimal == false) {
				number2 = new BigDecimal(tag);
			} else if (decimal == true) {
				number2 = new BigDecimal(tag).divide(BigDecimal.TEN);
				decimalTail++;
				decimalTail++;
			}
			number2set = true;
			tv.setText(number2.toString());
		} else if (operationset && number2set) {
			if (decimal == false) {
				number2 = number2.multiply(BigDecimal.TEN).multiply(
						BigDecimal.TEN);
			} else if (decimal == true) {
				number2 = number2.add(bigTag.divide(BigDecimal.TEN
						.pow(decimalTail)));
				decimalTail++;
				decimalTail++;
			}
			tv.setText(number2.toString());
		}

		return tag;

	}
}

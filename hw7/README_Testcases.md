# Instructions for running test cases:
The `testcases` directory comprises of a python script `run_tests.py` and test cases which are expected outputs for each scenario. The naming convention for the test case scenarios is as follows: `FLAG_number1_number2_so_on.exp`. For example, the test case for the scenario `$ java Chatbot 300 2110 4240` is `300_2110_4240.exp`. Do not modify any of these files.

To run the test cases, follow the below steps:

1. Extract all the files (python script and *.exp) in the directory with your Java source code.
2. Save the output generated by your code for each scenario to the file with same naming convention but with `.out` extension. For example, 
```
$ java Chatbot 300 2110 4240 > 300_2110_4240.out
```
3. To run the test cases for only a subset of flags, execute the python script with a list of flags separated by spaces as command line argument. For instance, to execute tests for flags 100 and 200, run:
```
$ python3 run_tests.py 100 200
```
4. To run all the test cases for all the flags, just run the python script without any flags as follows:
```
$ python3 run_tests.py
```

import os
import subprocess
import run_tests
print('~ DELETING OLD FILES.')
for filename in os.listdir("/afs/cs.wisc.edu/u/k/h/khai/private/cs540/hw10/"):
    if '.out' in filename:
        print('Deleting: ' + filename)
        os.remove(filename)

a = [   ["100_1","100 .1 .2 .3 .4 .5 .5 .6 .7 .8 .9 .9 .5 .2 1 0 1 0"],
        ["100_2","100 .021 .247 .35879 .1414 .75 .512 .686 .717 .818 .919 .029 .135 .20701 1 0 1 0"], 
        ["100_3", "100 0 .2 .3 .4 .5 0 .6 .7 .8 .9 0 .5 .2 0 0 0 0"],
        ['200_1', "200 .1 .2 .3 .4 .5 .5 .6 .7 .8 .9 .9 .5 .2 1 0 1 0 1"], 
        ["200_2", "200 -.1 .2 -.3 -.4 .5 -.5 -.6 -.7 .8 -.9 -.9 .5 -.2 -1 0 -1 0 1"],
        ["200_3", "200 .101 .809 .31 .9 .13 .55 .66 .12 .31 .1 .92 .05 .22 10 0 11.1 0.01 1"],
        ["300_1", "300 .1 .2 .3 .4 .5 .5 .6 .7 .8 .9 .9 .5 .2 1 0 1 0 1"], 
        ["300_2", "300 -.1 .2 -.3 -.4 .5 -.5 -.6 -.7 .8 -.9 -.9 .5 -.2 -1 0 -1 0 1"], 
        ["300_3", "300 .101 .809 .31 .9 .13 .55 .66 .12 .31 .1 .92 .05 .22 10 0 11.1 0.01 1"], 
        ["400_1", "400 .1 .2 .3 .4 .5 .5 .6 .7 .8 .9 .9 .5 .2 1 0 1 0 1"], 
        ["400_2", "400 -.1 .2 -.3 -.4 .5 -.5 -.6 -.7 .8 -.9 -.9 .5 -.2 -1 0 -1 0 1"], 
        ["400_3", "400 .101 .809 .31 .9 .13 .55 .66 .12 .31 .1 .92 .05 .22 10 0 11.1 0.01 1"], 
        ["500", "500 .1 .2 .3 .4 .5 .5 .6 .7 .8 .9 .9 .5 .2 .1"],
        ["600", "600 .1 .2 .3 .4 .5 .5 .6 .7 .8 .9 .9 .5 .2 .1"]
        ]

print('~ CREATING NEW .OUT FILES.')
for test in a:
        print('java NeuralNet ' + test[1])
        process = subprocess.Popen('java NeuralNet ' + test[1], stdout = subprocess.PIPE, shell = True)
        o = process.communicate()[0]
        f= open((test[0] + '.out'), 'wb')
        f.write(o)
        f.close()
        #process.kill()

print('~ CALLING run_tests.py.')
run_tests.run_all()
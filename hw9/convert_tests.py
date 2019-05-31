import os
import subprocess

source = ''
path = "/afs/cs.wisc.edu/u/k/h/khai/private/cs540/hw9/"
for filename in os.listdir(path):
    if ".class" in filename:
        source = filename.split('.')[0]
        break
for filename in os.listdir(path):
    if '.exp' in filename:
        temp = filename.split('.exp')
        temp = temp[0].split('_')
        a = [source] + temp
        a = " ".join(a) 
        print('$java ' + a)
        process = subprocess.Popen('java '+ a, stdout = subprocess.PIPE, shell = True)
        o = process.communicate()[0]
        f= open((filename.split('.exp')[0] + '.out'), 'wb')
        f.write(o)
        f.close
        #process.kill()
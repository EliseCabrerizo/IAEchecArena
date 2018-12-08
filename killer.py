import os
import argparse

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('pid')
    args = parser.parse_args()
    ppid=args.pid
    #commande="powershell.exe Stop-Process -Id "+args.pid
    commande="powershell.exe Get-Process -Name java > tmp2.txt"
    print(commande)
    listeP=os.system(commande)
    Rfichier=open("tmp2.txt","r")
    inputS=Rfichier.read();
    Rfichier.close()
    sp=inputS.split("\n")
    i=0
    for s in sp:
        #print(s)
        #print("==================")
        s.replace(" ","-:")
        if i > 2 :
            l=s.split(":")
            j=0
            for sl in l:
                #print("j = "+str(j)+" sl = "+sl)
                cleanS=""
                k=0
                while k < len(sl) :
                    if ord(sl[k]) != 32 :
                        if k > 1 and ord(sl[k-1]) == 32:
                            cleanS+=" "+sl[k]
                        else:
                            cleanS+=sl[k]
                    k+=1
                #print("cleanS = "+cleanS)
                if len(cleanS) > 2:
                    col=cleanS.split(" ")
                    pid=col[6]
                    #print("PID = "+pid)
                    if pid != ppid:
                        print("Killer "+pid)
                        killc="powershell.exe Stop-Process -Id "+pid
                        os.system(killc)
        i+=1


if __name__ == '__main__':
    main()

# Beispiel SVM-Prgramm

	&data
.text1
	abc\n
.text2
	XY

	&code
	nop
	mov $2, %reg(0)          # Funktion IO
	mov $1, %reg(1)          # stdout
	mov .text2, %reg(2)      # Startadresse
	mov len(.text2), %reg(3) # Länge der Ausgabe
	int 1                    # syscall
	nop
	mov $1, reg(0)          # exit
	mov $55, reg(1)         # return code
	int 1                   # syscall
	

# Arduino-UART / LDT 

This set of code should be pretty straightforward and easy to follow (hopefully).
The basis of it is that both microcontoller are in an off state until they get a connection on their
COM port. Each board deviates after that point.

	- Transmitter: Blocks until the Serial Buffer is not empty. Once it has something (from the PC),
	  it will read the incoming bytes bit by bit and toggle the GPIO port accordingly.
	  
	- Receiver: Blocks until it receives a 1 on digitalRead from the photodiode circuit. It will attempt
	to read in all the data and shift the bit stream into a usable byte.
	  
PCs -> Java application using Java Communications API (allows COM interactions in Java)
Note the Java console does NOT need to be used on the Receiver side, any other terminal emulator
should work fine. Just ensure your baud rate is correct.

If you have any questions feel free to email us via the contact button on the Members page
-> http://www.eecs.ucf.edu/seniordesign/fa2015sp2016/g32/

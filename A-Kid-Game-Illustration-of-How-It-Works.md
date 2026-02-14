## A Kid Game to Illustrate How Structural Messaging Work

###1. Players
    -- Message Sender as Encoder
    -- Message Receiver as Decoder
    -- Small Kid to generate random two bits when Mmssage sender asks
    -- Witnesses
    
###2. Message Sender and Receiver's Encoding/Decoding Tables

####Table I:   
        Per-word per-passiveHeadBits with 4 encodings 

        Hello ->  00 - skipLength=2
                  01 - skipLength=8
                  10 - skipLength=7
                  11 - skipLength=31
                    
        Alice ->  00 - skipLength=21
                  01 - skipLength=13
                  10 - skipLength=25
                  11 - skipLength=7
            ...            

####Table II:  
                
            Per-passive-head words list
                00 - Hello, Alice, Ski, Home,...
                01 - Hello, Alice, Dog,...
                10 - Hello, Alice, Frence, ...
                11 - Hello, Alice, Anybody,...
                
###3. Time Clicks in Sequence of Periods k=0, 1, 2, ...
      Everybody sees the current period number.
      
###4. Witnesses give a random message for Message Sender to send.
      Say "Hello, Alice".

----

###5. For Message Sender:

####5a) At time period k, Message Sender asks Small Kid to generate 2-bits code,
      
      The 2 bits are random: one out of 00 01 10 11.
      Say 01
       
      Show the 2-bits code to everyone.
      
####5b) Message Sender needs to send word "Hello" with passive code 01 he has to use.
      He/She checks Table I and get a skipLength=8.
      So he/she waite until Time Click adding 8 clicks.

####5c) Then, Message Sender ask Small Kid to generate 2-bits code to generate
      another header 2 bits and repeat step 4 and 5 to send next word "Alice".
      
      Repeat the until Message Sender has no more word to send. Then send "Stop".
   
----
   
###6. For Message Receiver:    
      
####6a) At each head 2 bits code anancement, he/she check Table II for next possible words to receive (1-to-n).

####6b) Next he/she check Table I for the time click number for each of possible words to receive.
        That is in a permutation tree of words style. Each new comming word will creates a ne child branch.

####6c) Then he/she just waits until a time click number for each of possible words to receive,
        to emit the word to validate.
        
####6d) If a sentece from permutation tree root to the currently receive word, has no meaning, drop this tree branch.
          
####6e) Until "Stop" is received. The collect all sentence from tree root to tree leaves. The message from the sender
        is one of them.

###7. To eliminate messages other than the sent message, just repeat 6 more than one time.
      Because the header 2 bits's randomness, only the real message from the sender can survive each of attempt.
      
###8, Done.   


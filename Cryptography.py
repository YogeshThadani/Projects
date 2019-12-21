import os
from builtins import bytearray


class KeyManager:
    armstrong_numbers = [153,370,371,407]
    base_table = [1234,1243,1324,1342,1423,1432,2134,2143,2314, 2341, 2413, 2431, 3124, 3142, 3214, 3241,3412, 3421, 4123, 4132, 4213, 4231, 4312, 4321]

    def __init__(self, remark):
        sum = self.__ascii_sum__(remark)
        permutation = KeyManager.base_table[sum % len(KeyManager.base_table)]
        portionA = self.__buildPortionA__(remark, permutation)
        self.xorkey = str(portionA) + str(sum)

    def get_xor_key(self):
        return self.xorkey

    def __ascii_sum__(self, remark):
        sum = 0
        for i in remark:
            sum+= ord(i)
        return sum

    def __buildPortionA__(self, remark, permutation):
        portionA = ''
        while permutation > 0:
            portionA = str(KeyManager.armstrong_numbers[permutation % 10 -1]) + portionA
            permutation = permutation//10
        return portionA


class Encryptor:
    def __init__(self, src, trgt, remark):
        if not os.path.exists(src):
            raise Exception('{0} file not found'.format(src) )
        self.src = src
        self.trgt = trgt
        self.km = KeyManager(remark)

    def encrypt(self):
        #open the source file for reading in binary mode
        fin = open(self.src, 'rb')
        # open the target file for writing in binary mode
        fout = open(self.trgt, 'wb')

        xor_key =self.km.get_xor_key()
        kl = len(xor_key)

        color = ColorManager.getColor(self.km.get_xor_key())
        cl = len(color)

        #bytearray (mutable)
        encoded = bytearray()
        i = 0
        r = 0
        c = 0
        data = 0

        while True:
            #buff to be loaded with 2048 or available (whichever less) bytes from the file
            #at EOF buff will be None
            buff = fin.read(2048)
            if buff:
                #process the bytes of the buff
                for abyte in buff:
                    #level1 encrypt
                    data = abyte ^ int(xor_key[i%kl])

                    #level2 encrypt
                    r = data >> 4 #higher nibble of the byte
                    c = data & 15 #lower nibble of the byte
                    data = (color[i%cl] + r * 16 + c) % 256 #color encrypt
                    i+=1
                    encoded.append(data)

                #write into the target file
                fout.write(encoded)
                #clear the encoded array for reuse
                encoded.clear()
            else:
                break

        fin.close()
        fout.close()


class Decryptor:
    def __init__(self, src, trgt, remark):
        if not os.path.exists(src):
            raise Exception('{0} file not found'.format(src))
        self.src = src
        self.trgt = trgt
        self.km = KeyManager(remark)

    def decrypt(self):
        # open the source file for reading in binary mode
        fin = open(self.src, 'rb')
        # open the target file for writing in binary mode
        fout = open(self.trgt, 'wb')

        xor_key = self.km.get_xor_key()
        kl = len(xor_key)

        color = ColorManager.getColor(self.km.get_xor_key())
        cl = len(color)

        # bytearray (mutable)
        decoded = bytearray()
        i = 0
        temp = 0
        r = 0
        c = 0
        data = 0
        while True:
            # buff to be loaded with 2048 or available (whichever less) bytes from the file
            # at EOF buff will be None
            buff = fin.read(2048)
            if buff:
                # process the bytes of the buff
                for abyte in buff:

                    # level2 encrypt
                    temp = (abyte - color[i%cl] + 256) % 256
                    r = temp // 16
                    c = temp % 16
                    data = (r << 4) | c #merge the nibbles to form a byte

                    # level1 encrypt
                    data = data ^ int(xor_key[i % kl])
                    i += 1
                    decoded.append(data)

                # write into the target file
                fout.write(decoded)
                # clear the encoded array for reuse
                decoded.clear()
            else:
                break

        fin.close()
        fout.close()

class ColorManager:
    @classmethod
    def getColor(cls, xor_key):
        r = (int(xor_key[0:4]) + int(xor_key[12:])) %256
        g = (int(xor_key[4:8]) + int(xor_key[12:])) %256
        b = (int(xor_key[8:12]) + int(xor_key[12:])) %256
        return r,g,b
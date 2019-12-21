import Cryptography

def main():
    #org = 'd:\Tulips.jpg'
    #encr = 'd:\enc_Tulips.jpg'
    #regained = 'd:\Tulips_again.jpg'

    org = 'd:\\img.JPG'
    encr = 'd:\\enc_b.JPG'
    regained = 'd:\\b_again.JPG'

    remark = 'How old is my computer?'
    try:
        enc = Cryptography.Encryptor(org, encr, remark)
        enc.encrypt()
        dec = Cryptography.Decryptor(encr, regained, remark)
        dec.decrypt()
    except Exception as e:
        print(e)

main()
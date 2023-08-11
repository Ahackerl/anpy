import re

def test():
    results = re.split('，','你好，世界')
    return results


print(test())
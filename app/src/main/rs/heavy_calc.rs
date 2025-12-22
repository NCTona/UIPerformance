#pragma version(1)
#pragma rs java_package_name(com.tona.myapplication.threaddemo)

int RS_KERNEL heavyCalc(int in) {
    int sum = 0;
    for (int i = 0; i < 1_000_000; i++) {
        sum += i;
    }
    return sum;
}

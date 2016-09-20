#include <unistd.h>
#include <stdio.h>

int main(int argc, char* argv[]) {
 if (argc != 2) {
  fprintf(stderr,"Use: hunlink <dir>\n");
  return 1;
 }
 int ret = unlink(argv[1]);
 if (ret != 0)
  perror("unlink");
 return ret;
}

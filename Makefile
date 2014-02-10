all:clean
	./gen

run:all
	./ewp_shell

debug:all
	./ewp_shell -debug

clean:
	rm -rf ./ewp_shell

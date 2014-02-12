all:clean
	./gen

plain:clean
	./gen plain

run:all
	./ewp_shell

debug:all
	./ewp_shell -debug

clean:
	rm -rf ./ewp_shell

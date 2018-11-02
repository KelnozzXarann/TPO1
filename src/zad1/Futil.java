package zad1;

//Zadanie: kanały plikowe
//
//        Katalog {user.home}/TPO1dir  zawiera pliki tekstowe umieszczone w tym katalogu i jego różnych podkatalogach. Kodowanie plików to Cp1250.
//        Przeglądając rekursywnie drzewo katalogowe, zaczynające się od {user.home}/TPO1dir,  wczytywać te pliki i dopisywać ich zawartości do pliku o
//        nazwie TPO1res.txt, znadującym się w katalogu projektu. Kodowanie pliku TPO1res.txt winno być UTF-8, a po każdym uruchomieniu programu plik
//        ten powinien zawierać tylko aktualnie przeczytane dane z  plików katalogu/podkatalogow.
//
//        Poniższy gotowy fragment winien wykonać całą robotę:
//
//public class Main {
//    public static void main(String[] args) {
//        String dirName = System.getProperty("user.home")+"/TPO1dir";
//        String resultFileName = "TPO1res.txt";
//        Futil.processDir(dirName, resultFileName);
//    }
//}
//
//Uwagi:
//
//        pliku Main.java nie wolno w żaden sposób modyfikować,
//        trzeba dostarczyć definicji klasy Futil,
//        oczywiście, nazwa katalogu i pliku oraz ich położenie są obowiązkowe,
//        należy zastosować FileVisitor do przeglądania katalogu oraz kanały plikowe (klasa FileChannel) do odczytu/zapisu plików (bez tego rozwiązanie nie uzyska punktów).
//        w wynikach testów mogą być przedstawione dodatkowe zalecenia co do sposobu wykonania zadania (o ile rozwiązanie nie będzie jeszcze ich uwzględniać),.
//



import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {

    private static final String resultFile = System.getProperty("user.dir")+"/";

    public static void processDir(String dirName, String resultFileName) {
        final Path path= Paths.get(dirName);
        final Path outputPath=Paths.get(resultFile+resultFileName);
        if(Files.exists(outputPath)){
            try {
                Files.delete(outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    RandomAccessFile inputFile=new RandomAccessFile(file.toFile(), "r");
                    FileChannel fc =inputFile.getChannel();
                    FileChannel fcout =FileChannel.open(outputPath,StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.APPEND);

                    ByteBuffer buf = ByteBuffer.allocate((int)fc.size());
                    int bytesRead = fc.read(buf);
                    buf.flip();
                    CharBuffer charBuffer = Charset.forName("CP1250").decode(buf);
                    buf=Charset.forName("UTF-8").encode(charBuffer);
                    fcout.write(buf);
                    fcout.close();
                    fc.close();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

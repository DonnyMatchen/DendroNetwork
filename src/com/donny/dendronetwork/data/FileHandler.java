package com.donny.dendronetwork.data;

import com.donny.dendronetwork.DendroNetwork;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileHandler {

    public FileHandler() {
        DendroNetwork.LOG_HANDLER.trace(getClass(), "FileHandler Initiated");
    }

    public String read(File file) {
        ensure(file.getParentFile());
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, Charset.forName("unicode")))) {
            boolean flag = true;
            while (flag) {
                int x = reader.read();
                if (x != -1) {
                    output.append((char) x);
                } else {
                    flag = false;
                }
            }
            DendroNetwork.LOG_HANDLER.debug(getClass(), "file read: " + file.getAbsolutePath());
            return output.toString().replace("\r", "");
        } catch (IOException e) {
            if (file.exists()) {
                DendroNetwork.LOG_HANDLER.error(getClass(), file.getPath() + " could not be read from");
            } else {
                DendroNetwork.LOG_HANDLER.error(getClass(), file.getPath() + " does not exist");
            }
            return "";
        }
    }

    public String read(File dir, String file) {
        return read(new File(dir.getPath() + File.separator + file));
    }

    public String readPlain(File file) {
        ensure(file.getParentFile());
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.US_ASCII))) {
            boolean flag = true;
            while (flag) {
                int x = reader.read();
                if (x != -1) {
                    output.append((char) x);
                } else {
                    flag = false;
                }
            }
            reader.close();
            DendroNetwork.LOG_HANDLER.debug(getClass(), "file read: " + file.getAbsolutePath());
            return output.toString().replace("\r", "");
        } catch (IOException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), file.getPath() + " could not be read from");
            return "";
        }
    }

    public String readPlain(File dir, String file) {
        return readPlain(new File(dir.getPath() + File.separator + file));
    }

    public byte[] getResource(String path) {
        try (InputStream stream = getClass().getResourceAsStream("/com/donny/dendronetwork/resources/" + path)) {
            return stream.readAllBytes();
        } catch (IOException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), "Resource not located: " + path);
            return null;
        } catch (NullPointerException ex) {
            DendroNetwork.LOG_HANDLER.error(getClass(), "No such resource: " + path);
            return null;
        }
    }

    public void write(File file, String output) {
        ensure(file.getParentFile());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, Charset.forName("unicode")))) {
            writer.write(output);
            writer.close();
            DendroNetwork.LOG_HANDLER.debug(getClass(), "file written: " + file.getAbsolutePath());
        } catch (IOException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), file.getPath() + " could not be written to");
            DendroNetwork.LOG_HANDLER.debug(getClass(), output);
        }
    }

    public void write(File dir, String file, String output) {
        write(new File(dir.getPath() + File.separator + file), output);
    }

    public void writePlain(File file, String output) {
        ensure(file.getParentFile());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.US_ASCII))) {
            writer.write(output);
            writer.close();
            DendroNetwork.LOG_HANDLER.debug(getClass(), "file written: " + file.getAbsolutePath());
        } catch (IOException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), file.getPath() + " could not be written to");
            DendroNetwork.LOG_HANDLER.debug(getClass(), output);
        }
    }

    public void writePlain(File dir, String file, String output) {
        writePlain(new File(dir.getPath() + File.separator + file), output);
    }

    public void append(File file, String output) {
        ensure(file.getParentFile());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, Charset.forName("unicode"), true))) {
            writer.write(output);
            writer.close();
            DendroNetwork.LOG_HANDLER.debug(getClass(), "file appended: " + file.getAbsolutePath());
        } catch (IOException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), file.getPath() + " could not be written to");
            DendroNetwork.LOG_HANDLER.debug(getClass(), output);
        }
    }

    public void append(File dir, String file, String output) {
        append(new File(dir.getPath() + File.separator + file), output);
    }

    public void appendPlain(File file, String output) {
        ensure(file.getParentFile());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.US_ASCII, true))) {
            writer.write(output);
            writer.close();
            DendroNetwork.LOG_HANDLER.debug(getClass(), "file appended: " + file.getAbsolutePath());
        } catch (IOException e) {
            DendroNetwork.LOG_HANDLER.error(getClass(), file.getPath() + " could not be written to");
            DendroNetwork.LOG_HANDLER.debug(getClass(), output);
        }
    }

    public void appendPlain(File dir, String file, String output) {
        appendPlain(new File(dir.getPath() + File.separator + file), output);
    }

    public void delete(File file) {
        ensure(file.getParentFile());
        file.delete();
        DendroNetwork.LOG_HANDLER.debug(getClass(), "file deleted: " + file.getAbsolutePath());
    }

    public void delete(File dir, String file) {
        delete(new File(dir.getPath() + File.separator + file));
    }

    public void deleteR(File root) {
        File[] rootList = root.listFiles();
        if (root.isDirectory() && rootList != null) {
            for (File f : rootList) {
                deleteR(f);
            }
        }
        delete(root);
    }

    public void ensure(File file) {
        if (!file.exists()) {
            if (file.getParentFile().exists()) {
                file.mkdir();
            } else {
                ensure(file.getParentFile());
            }
        }
    }
}

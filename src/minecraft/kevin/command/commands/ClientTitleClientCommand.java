package kevin.command.commands;

import java.util.Arrays;
import java.util.Collections;

import kevin.command.IClientCommand;
import kevin.utils.ChatUtils;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;

public final class ClientTitleClientCommand implements IClientCommand {
    @NotNull
    public static final ClientTitleClientCommand INSTANCE = new ClientTitleClientCommand();

    // Anti Initialize, use INSTANCE instead
    private ClientTitleClientCommand() {
    }

    public void run(@Nullable String[] args) {
        if (args != null && args.length != 0) {
            StringBuilder tmp = new StringBuilder();

            for(int i =1; i< args.length; i++){
                tmp.append(args[i])
                        .append(" ");
            }
            Display.setTitle(tmp.toString());
            ChatUtils.messageWithStart("Current window title: <" + tmp + '>');
        } else {
            ChatUtils.messageWithStart("Please, use \".ClientTitle <title>\"");
        }
    }
}

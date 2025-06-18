package kevin.module.modules.misc;

import kevin.event.impl.AttackEvent;
import kevin.event.EventTarget;
import kevin.event.UpdateEvent;
import kevin.event.WorldEvent;
import kevin.main.KevinClient;
import kevin.module.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Automatically say something when you killed a player.
 */
public final class AutoL extends ClientModule {

    private static final AutoL INSTANCE = new AutoL();
    private final List<String> modeList = new ArrayList<>();
    private final String fileSuffix = ".txt";

    // Values
    private final ListValue modeValue = new ListValue("Mode",
            modeList.toArray(new String[0]), "Single");

    private final ListValue prefix = new ListValue(
            "Prefix",
            new String[]{
                    "None",
                    "/shout",
                    ".",
                    "@",
                    "!",
                    "Custom"
            }, "None"
    );
    private final TextValue customPrefix = new TextValue("CustomPrefix",
            ""
    );

    private final TextValue singleMessage = new TextValue("SingleMessage",
            "%MyName killed %name"
    );
    private final FloatValue flood = new FloatValue(
            "Flood",
            1f, 0f, 5f
    );
    // Attack tracking
    private final List<EntityPlayer> entityList = new ArrayList<>();

    private final Random random = new Random();


    // Custom messages
    private final String[] skidMaMSG = {
            "Sigma made this world a better place, killing you with it even more",
            "My whole life changed since I discovered Sigma",
            "Learn your alphabet with the Sigma client: Panda, Sigma, Epsilon, Alpha!",
            "Why Sigma? Cause it is the addition of pure skill and incredible intellectual abilities",
            "Sigma Client users be like: Hit or miss I guess I never miss!",
            "Stop it, get some help! Get Sigma",
            "Hypixel wants to know Sigma Client owner's location [Accept] [Deny]",
            "I don't miss hit, i see you miss that",
            "I don't hack i just have Sigma Gaming Chair",
            "How are you so bad? just practice your aim and hold w",
            "Did I really just forget that melody? Si sig sig sig Sigma"
    };

    // 感谢陈欣蘅提供的cumMSG
    private final String[] cumMSG = {
            "呐呐~杂鱼哥哥不会这样就被捉弄的不会说话了吧 真是弱哎~",
            "嘻嘻~杂鱼哥哥不会以为竖个大拇哥就能欺负我了吧~不会吧~不会吧~杂鱼哥哥怎么可能欺负",
            "哥哥真是好欺负啊~嘻嘻~",
            "哎~杂鱼说话就是无趣唉~只会发一张表情包的笨蛋到处都有吧",
            "呐呐~杂鱼哥哥发这个是想教育我吗~嘻嘻~怎么可能啊~",
            "欸？你这个杂鱼~又来问我问题了吗",
            "你这种杂鱼~怎么有资格和我说话？",
            "哥哥这么短~根本没感觉的好吧!",
            "我就是喜欢捉弄这样笨笨的大叔哦~",
            "想带我走?大叔不会是想做色涩的事情吧~",
            "大叔是没有萝莉控吗？要不然怎么天天围着我转呀，好恶心~"
    };

    private File[] messageFiles;

    /**
     * Constructor for AutoL module.
     */
    public AutoL() {
        super("AutoL",
                "Send messages automatically when you kill a player.",
                ModuleCategory.MISC);

        modeList.add("Single");
        modeList.add("SkidMa");
        modeList.add("Cum");

        File dir = KevinClient.fileManager.killMessages;
        if (dir.exists() && dir.isDirectory()) {
            messageFiles = dir.listFiles(file -> file.getName().endsWith(fileSuffix));
            if (messageFiles != null) {
                for (File file : messageFiles) {
                    String name = file.getName().split("\\.")[0];
                    modeList.add(name);
                }
            }
        }

    }

    /**
     * Get the instance
     * @return the singleton instance of AutoL
     */
    public static AutoL getInstance() {
        return INSTANCE;
    }

    /**
     * Clear the entity list when the world changes.
     * @param event
     */
    @EventTarget
    public void onWorld(WorldEvent event) {
        entityList.clear();
    }

    /**
     * Add to list when a player got attacked.
     * @param event
     */
    @EventTarget
    public void onAttack(AttackEvent event) {
        if (event.getTargetEntity() instanceof EntityPlayer target) {
            if (!entityList.contains(target)) {
                entityList.add(target);
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        List<EntityPlayer> toRemove = new ArrayList<>();
        for (EntityPlayer player : entityList) {
            if (player.isDead) {
                toRemove.add(player);
                String message = getMessageBasedOnMode();
                String formattedMessage = addPrefix(message).replace("%MyName",
                        Minecraft.getMinecraft().thePlayer.getName()).replace("%name",
                        repeatString(player.getName(), flood.get().intValue()));
                Minecraft.getMinecraft().thePlayer.sendChatMessage(formattedMessage);
            }
        }
        entityList.removeAll(toRemove);
    }

    private String getMessageBasedOnMode() {
        // Old java compatible old switch statement
        //noinspection EnhancedSwitchMigration
        switch (modeValue.get().toLowerCase()) {
            case "single":
                return singleMessage.get();
            case "skidma":
                return skidMaMSG[random.nextInt(skidMaMSG.length)];
            case "cum":
                return cumMSG[random.nextInt(cumMSG.length)];
            default:
                for (File file : messageFiles) {
                    if (file.getName().replace(fileSuffix, "").equals(modeValue.get())) {
                        try {
                            List<String> lines = Files.readAllLines(file.toPath());
                            return lines.get(random.nextInt(lines.size()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return "";
        }
    }

    private String addPrefix(String message) {
        //noinspection EnhancedSwitchMigration
        switch (prefix.get()) {
            case "/shout":
                return "/shout " + message;
            case ".":
                return ".say " + message;
            case "@":
                return "@" + message;
            case "!":
                return "!" + message;
            case "Custom":
                return customPrefix.get() + message;
            default:
                return message;
        }
    }

    private String repeatString(String str, int count) {
        return String.valueOf(str).repeat(Math.max(0, count));
    }
}
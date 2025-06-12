package kevin.module.modules.misc;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import kevin.event.EventTarget;
import kevin.event.UpdateEvent;
import kevin.main.KevinClient;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.IntegerValue;
import kevin.module.ListValue;
import kevin.module.ModuleCategory;
import kevin.module.TextValue;
import kevin.utils.RandomUtils;
import kevin.utils.system.timer.MSTimer;
import kevin.utils.system.timer.TimeUtils;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// $VF: Compiled from SuperSpammer.kt

public final class SuperSpammer extends ClientModule {
    @NotNull
    private final ArrayList<String> modeList;
    @NotNull
    private final String fileSuffix;
    @NotNull
    private String[] modeListArray;
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final IntegerValue maxDelayValue;
    @NotNull
    private final IntegerValue minDelayValue;
    @NotNull
    private final TextValue messageValue;
    @NotNull
    private final TextValue switchMessage1;
    @NotNull
    private final TextValue switchMessage2;
    @NotNull
    private final BooleanValue randomCharacterAtFirst;
    @NotNull
    private final BooleanValue randomCharacterAtLast;
    @NotNull
    private final IntegerValue firstMaxLength;
    @NotNull
    private final IntegerValue firstMinLength;
    private int firstLength;
    @NotNull
    private final IntegerValue lastMaxLength;
    @NotNull
    private final IntegerValue lastMinLength;
    private int lastLength;
    @NotNull
    private final ListValue startMode;
    @NotNull
    private final TextValue customPrefix;
    @NotNull
    private final TextValue firstLeft;
    @NotNull
    private final TextValue firstRight;
    @NotNull
    private final TextValue lastLeft;
    @NotNull
    private final TextValue lastRight;
    @NotNull
    private final BooleanValue customNoRandomV;
    @NotNull
    private final BooleanValue autoDisableV;
    @NotNull
    private final MSTimer msTimer;
    private long delay;
    private int sentencesNumber;
    private int switchState;
    @Nullable
    private String lastMode;
    @NotNull
    private final ArrayList<String> nvJiangShiList;
    @NotNull
    private final ArrayList<String> huaQiangList;
    @NotNull
    private final ArrayList<String> jieGeList;
    @NotNull
    private final ArrayList<String> niKanDaoDeWoList;
    @NotNull
    private final ArrayList<String> aliezList;
    @NotNull
    private final ArrayList<String> shuoJuShiHuaList;
    @NotNull
    private final ArrayList<String> invincibleList;

    public SuperSpammer() {
        super("SuperSpammer", "Spams the chat with given messages.", ModuleCategory.MISC);// 31 32 33
        String[] files = new String[]{
                "Single",
                "Switch",// 37
                "\u534e\u5f3a\u4e70\u74dc",// 38
                "\u7cbe\u901a\u4eba\u6027\u7684\u5973\u8bb2\u5e08",// 39
                "\u6770\u54e5\u4e0d\u8981",// 40
                "\u4f60\u770b\u5230\u7684\u6211",// 41
                "AlieZ",// 42
                "\u8bf4\u53e5\u5b9e\u8bdd",// 43
                "Invincible"// 44
        };
        this.modeList = CollectionsKt.arrayListOf(files);// 35 36
        this.fileSuffix = ".txt";// 46
        Collection $this$toTypedArray$iv = (Collection)this.modeList;

        this.modeListArray = (String[])$this$toTypedArray$iv.toArray(new String[0]);// 47 657 658
        File filesx = KevinClient.INSTANCE.getFileManager().spammerDir;// 50
        File[] spammerFiles = filesx.listFiles(it -> {
            String var10000 = it.getName();// 52
            return StringsKt.endsWith(var10000, fileSuffix, false);
        });// 51
        if (spammerFiles != null) {// 53
            Iterator $this$toTypedArray$ivx = ArrayIteratorKt.iterator(spammerFiles);

            while ($this$toTypedArray$ivx.hasNext()) {
                File i = (File)$this$toTypedArray$ivx.next();
                ArrayList var10000 = this.modeList;
                String var10001 = i.getName();
                Intrinsics.checkNotNullExpressionValue(var10001, "getName(...)");
                CharSequence var19 = (CharSequence)var10001;
                String[] thisCollection$iv = new String[]{".txt"};
                var10000.add(StringsKt.split(var19, thisCollection$iv, false,0).get(0));
            }
        }

        Collection $this$toTypedArray$ivx = (Collection)this.modeList;
        int $i$f$toTypedArrayx = 0;
        this.modeListArray = (String[])$this$toTypedArray$ivx.toArray(new String[0]);// 54 659 660
        this.modeValue = new ListValue("Mode", this.modeListArray, "Single");// 57
        this.maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 5000)      // $VF: Compiled from SuperSpammer.kt// 60
        {
            protected void onChanged(int oldValue, int newValue) {
                int minDelayValueObject = ((Number)SuperSpammer.this.minDelayValue.get()).intValue();// 62
                if (minDelayValueObject > newValue) {// 63
                    this.set(minDelayValueObject);
                }

                SuperSpammer.this.delay = TimeUtils.randomDelay(((Number)SuperSpammer.this.minDelayValue.get()).intValue(), ((Number)this.get()).intValue());// 64
            }// 65
        };
        this.minDelayValue = new IntegerValue("MinDelay", 500, 0, 5000)      // $VF: Compiled from SuperSpammer.kt// 69
        {
            protected void onChanged(int oldValue, int newValue) {
                int maxDelayValueObject = ((Number)SuperSpammer.this.maxDelayValue.get()).intValue();// 71
                if (maxDelayValueObject < newValue) {// 72
                    this.set(maxDelayValueObject);
                }

                SuperSpammer.this.delay = TimeUtils.randomDelay(((Number)this.get()).intValue(), ((Number)SuperSpammer.this.maxDelayValue.get()).intValue());// 73
            }// 74
        };
        this.messageValue = new TextValue("SingleMessage", KevinClient.INSTANCE.getName() + " Client | Jiege");// 78
        this.switchMessage1 = new TextValue("SwitchMessageFirst", "https://space.bilibili.com/1372772553 Liquid_Bounce-\u6770\u54e5");// 79 80 81
        this.switchMessage2 = new TextValue("SwitchMessageSecond", "Liquid_Bounce-\u6770\u54e5 https://space.bilibili.com/1372772553");// 83 84 85
        this.randomCharacterAtFirst = new BooleanValue("RandomCharacterAtFirst", true);// 88
        this.randomCharacterAtLast = new BooleanValue("RandomCharacterAtLast", true);// 89
        
        this.firstMaxLength = new IntegerValue("RandomCharacterAtFirstMaxLength", 3, 1, 7)      // $VF: Compiled from SuperSpammer.kt// 93
        {
            protected void onChange(int oldValue, int newValue) {
                int min = ((Number)SuperSpammer.this.firstMinLength.get()).intValue();// 95
                if (min > newValue) {// 96
                    this.set(min);
                }

                SuperSpammer.this.firstLength = RandomUtils.nextInt(((Number)SuperSpammer.this.firstMinLength.get()).intValue(), ((Number)this.get()).intValue());// 97
                super.onChange(oldValue, newValue);// 98
            }// 99
        };
        
        this.firstMinLength = new IntegerValue("RandomCharacterAtFirstMinLength", 1, 1, 7)      // $VF: Compiled from SuperSpammer.kt// 102
        {
            protected void onChange(int oldValue, int newValue) {
                int max = ((Number)SuperSpammer.this.firstMaxLength.get()).intValue();// 104
                if (max < newValue) {// 105
                    this.set(max);
                }

                SuperSpammer.this.firstLength = RandomUtils.nextInt(((Number)this.get()).intValue(), ((Number)SuperSpammer.this.firstMaxLength.get()).intValue());// 106
                super.onChange(oldValue, newValue);// 107
            }// 108
        };
        this.firstLength = RandomUtils.nextInt(((Number)this.firstMinLength.get()).intValue(), ((Number)this.firstMaxLength.get()).intValue());// 111
       
        this.lastMaxLength = new IntegerValue("RandomCharacterAtLastMaxLength", 3, 1, 7)      // $VF: Compiled from SuperSpammer.kt// 115
        {
            protected void onChange(int oldValue, int newValue) {
                int min = ((Number)SuperSpammer.this.lastMinLength.get()).intValue();// 117
                if (min > newValue) {// 118
                    this.set(min);
                }

                SuperSpammer.this.lastLength = RandomUtils.nextInt(((Number)SuperSpammer.this.lastMinLength.get()).intValue(), ((Number)this.get()).intValue());// 119
                super.onChange(oldValue, newValue);// 120
            }// 121
        };
        this.lastMinLength = new IntegerValue("RandomCharacterAtLastMinLength", 1, 1, 7)      // $VF: Compiled from SuperSpammer.kt// 124
        {
            protected void onChange(int oldValue, int newValue) {
                int max = ((Number)SuperSpammer.this.lastMaxLength.get()).intValue();// 126
                if (max < newValue) {// 127
                    this.set(max);
                }

                SuperSpammer.this.lastLength = RandomUtils.nextInt(((Number)this.get()).intValue(), ((Number)SuperSpammer.this.lastMaxLength.get()).intValue());// 128
                super.onChange(oldValue, newValue);// 129
            }// 130
        };
        this.lastLength = RandomUtils.nextInt(((Number)this.lastMinLength.get()).intValue(), ((Number)this.lastMaxLength.get()).intValue());// 133
        String[] var16 = new String[]{"None", "/shout", ".", "@", "!", "Custom"};// 137
        String[] var10004 = ClientModule.arrayOf(var16);
        Intrinsics.checkNotNullExpressionValue(var10004, "arrayOf(...)");
        this.startMode = new ListValue("Prefix", var10004, "None");// 135 136 138
        this.customPrefix = new TextValue("CustomPrefix", "");// 140
        this.firstLeft = new TextValue("RandomCharacterAtFirstLeft", "[");// 141
        this.firstRight = new TextValue("RandomCharacterAtFirstRight", "]");// 142
        this.lastLeft = new TextValue("RandomCharacterAtLastLeft", "[");// 143
        this.lastRight = new TextValue("RandomCharacterAtLastRight", "]");// 144
        this.customNoRandomV = new BooleanValue("CustomNoRandom", true);// 145
        this.autoDisableV = new BooleanValue("AutoDisable", false);// 146
        this.msTimer = new MSTimer();// 148
        this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());// 150
        this.switchState = 1;// 152
        files = new String[]{
                "<\u4e09\u53e5\u8bdd\uff0c\u8ba9\u7537\u4eba\u4e3a\u6211\u82b1\u4e86\u5341\u516b\u4e07>",
                "\u6211\u662f\u4e00\u4e2a\u5f88\u5584\u4e8e\u8ba9\u7537\u4eba\u4e3a\u6211\u82b1\u94b1\u7684\u7cbe\u901a\u4eba\u6027\u7684\u5973\u8bb2\u5e08",// 244
                "\u524d\u4e24\u5929\u5462\u6211\u4e0e\u4e00\u4e2a\u7537\u6027\u7684\u670b\u53cb\u5403\u996d",// 245
                "\u5f53\u6211\u5750\u4e0b\u6765\u7684\u65f6\u5019\u6211\u76f4\u63a5\u95ee\u4e86\u4e00\u53e5\uff1a\u54c7\u585e\u6211\u4eca\u5929\u597d\u6f02\u4eae\uff0c\u7ed9\u4f60\u4e2a\u673a\u4f1a\u5938\u5938\u6211",// 246
                "\u4ed6\u54c8\u54c8\u5927\u7b11\uff0c\u4e00\u65f6\u534a\u4f1a\u513f\u5462\u90fd\u6ca1\u6709\u56de\u8fc7\u795e\u6765",// 247
                "\u8fd9\u79cd\u5462\u5c31\u662f\u5178\u578b\u7684\u76f4\u7537",// 248
                "\u7136\u540e\u6211\u5750\u4e0b\u6765\u7ee7\u7eed\u95ee\uff1a\u6211\u4eec\u73a9\u4e2a\u95ee\u7b54\u6e38\u620f\u5427",// 249
                "\u4ed6\u8bf4\uff1a\u4f60\u95ee\u6211\u7b54",// 250
                "\u6211\u8bf4 \uff1a\u4f60\u77e5\u9053\u5728\u6211\u773c\u91cc\u4f60\u4ec0\u4e48\u65f6\u5019\u6700\u5e05\u5417",// 251
                "\u4ed6\u8bf4\u6211\u4e0d\u77e5\u9053\uff0c\u6240\u4ee5\u76f4\u7537\u5f88\u65e0\u8da3",// 252
                "\u666e\u901a\u5973\u4eba\u5462\u8fd9\u65f6\u5019\u4f1a\u8bf4\u4f60\u4e3a\u6211\u4e70\u5355\u7684\u65f6\u5019\u6700\u5e05",// 253
                "\u4f46\u662f\u6211\u8bf4\u4ec0\u4e48\u5462 \u4f60\u4e3a\u6211\u62ff\u9910\u5177 \u70b9\u83dc \u5939\u83dc \u4e70\u5355 \u63d0\u5305\u7684\u65f6\u5019\u6700\u5e05",// 254
                "\u4ed6\u53c8\u662f\u4e00\u4efd\u610f\u60f3\u4e0d\u5230\u7684\u72c2\u559c\uff0c\u63a5\u4e0b\u6765\u7684\u5168\u7a0b\u5462\u6211\u662f\u4ec0\u4e48\u4e5f\u4e0d\u7528\u5e72\uff0c\u4ed6\u8fd8\u5c41\u98a0\u5c41\u98a0\u7684\u4e3a\u6211\u670d\u52a1",// 255
                "\u5403\u5230\u6700\u540e\u6211\u8bf4\u6765\u4f60\u7ed9\u6211\u6765\u4e00\u53ea\u9f99\u867e\u5956\u52b1\u6211\u8fd9\u4e48\u6709\u773c\u5149\u8ddf\u5929\u4e0b\u7b2c\u4e00\u5e05\u7684\u7ec5\u58eb\u7537\u5403\u996d\uff0c\u597d\u5f00\u5fc3\u554a",// 256
                "\u6700\u540e\uff0c\u4ed6\u975e\u5e38\u5f00\u5fc3\u7684\u5c31\u628a\u5355\u7ed9\u4e70\u4e86",// 257
                "\u8fd9\u4e00\u9910\u996d\u6211\u4eec\u82b1\u4e86\u5341\u4e94\u4e07\u516b\u5343\u516d",// 258
                "\u56de\u5230\u5bb6\u7684\u65f6\u5019\u6211\u6253\u5f00\u624b\u673a\u4ee5\u770b\u8fd9\u4e2a\u7537\u4eba\u7ed9\u6211\u8f6c\u4e86\u4e00\u4e2a\u4e00\u4e07\u516b\u5343\u516b\u7684\u7ea2\u5305\uff0c\u8bf4\u4e86\u4e00\u53e5\u548c\u4f60\u5728\u4e00\u8d77\u771f\u5f00\u5fc3",// 259
                "\u4e00\u4e2a\u5973\u4eba\u8bf4\u8bdd\u6709\u8da3\u5f88\u91cd\u8981\uff0c\u4f1a\u8c03\u620f\u7537\u4eba\u66f4\u91cd\u8981",// 260
                "\u5148\u656c\u4e8e\u793c\u4e50\u91ce\u4eba\u4e5f\uff0c\u540e\u656c\u4e8e\u793c\u4e50\u541b\u5b50\u4e5f"// 261
        };
        this.nvJiangShiList = CollectionsKt.arrayListOf(files);// 242 243
        files = new String[]{
                "<\u6709\u4e00\u4e2a\u4eba\u524d\u6765\u4e70\u74dc>",
                "\u751f\u5f02\u5f62\u5417\u4f60\u4eec\u54e5\u51e0\u4e2a\uff0c\u54e5\u4fe9",// 265
                "\u54e5\u4eec\uff0c\u4f60\u8fd9\u74dc\u591a\u5c11\u94b1\u4e00\u65a4\u554a",// 266
                "\u4e24\u5757\u94b1\u4e00\u65a4",// 267
                "What's Up \u8fd9\u74dc\u76ae\u5b50\u662f\u91d1\u5b50\u505a\u7684\u8fd8\u662f\u74dc\u7c92\u5b50\u662f\u91d1\u5b50\u505a\u7684",// 268
                "\u4f60\u77a7\u77a7\u8fd9\u73b0\u5728\u54ea\u513f\u6709\u74dc\u5440\uff0c\u8fd9\u90fd\u662f\u5927\u68da\u7684\u74dc\uff0c\u4f60\u5acc\u8d35\u6211\u8fd8\u5acc\u8d35\u5462",// 269
                "\u7ed9\u6211\u6311\u4e00\u4e2a",// 270
                "\u884c",// 271
                "\u8fd9\u4e2a\u600e\u4e48\u6837",// 272
                "\u4f60\u8fd9\u74dc\u4fdd\u719f\u5417",// 273
                "\u6211\u5f00\u6c34\u679c\u644a\u7684\uff0c\u80fd\u5356\u7ed9\u4f60\u751f\u74dc\u86cb\u5b50",// 274
                "\u6211\u95ee\u4f60\u8fd9\u74dc\u4fdd\u719f\u5417",// 275
                "\u4f60\u662f\u6545\u610f\u627e\u832c\u662f\u4e0d\u662f\u554a\uff0c\u4f60\u8981\u4e0d\u8981\u5427",// 276
                "\u8fd9\u74dc\u8981\u719f\u6211\u80af\u5b9a\u8981\u554a\uff0c\u90a3\u4ed6\u8981\u662f\u4e0d\u719f\u600e\u4e48\u529e\u554a",// 277
                "\u54ce\uff0c\u8981\u662f\u4e0d\u719f\uff0c\u6211\u81ea\u5df1\u5403\u4e86\u5b83\uff0c\u6ee1\u610f\u4e86\u5427",// 278
                "\u5341\u4e94\u65a4\uff0c\u4e09\u5341\u5757",// 279
                "\u4f60\u8fd9\u54ea\u591f\u5341\u4e94\u65a4\u554a\u4f60\u8fd9\u79f0\u6709\u95ee\u9898\u554a",// 280
                "\u4f60TM\u6545\u610f\u627e\u832c\u662f\u4e0d\u662f\u554a\uff0c\u4f60\u8981\u4e0d\u8981\u5427\uff0c\u4f60\u8981\u4e0d\u8981",// 281
                "\u5438\u94c1\u77f3\uff0c\u53e6\u5916\u4f60\u8bf4\u7684\u8fd9\u74dc\u8981\u662f\u751f\u7684\u4f60\u81ea\u5df1\u541e\u8fdb\u53bb\u554a",// 282
                "\uff08\u5288\u74dc\uff09\uff01",// 283
                "\u4f60TM\u5288\u6211\u74dc\u662f\u5427",// 284
                "\u6211\uff0cchi\u2198\uff01\uff08\u5288\u4eba\uff09",// 285
                "\u54ce\uff0c\u6740\u4eba\u5566\uff0c\u6740\u4eba\u5566",// 286
                "\u54ce\uff0c\u534e\u5f3a\uff0c\u534e\u5f3a\uff01\uff08\u56de\u5934\u4e00\u7b11\uff09"// 287
        };
        this.huaQiangList = CollectionsKt.arrayListOf(files);// 263 264
        files = new String[]{
                "\u300a\u6770\u54e5\u4e0d\u8981\u300b",
                "\u963f\u4f1f\uff1a\u5f31\u6b38\uff0c\u62dc\u6258\u4f60\u5f88\u5f31\u6b38\uff0c\u73b0\u5728\u77e5\u9053\u8c01\u662f\u8001\u5927\u4e86\u54e6\uff0c\u54c8\uff01",// 291
                "\u963f\u5af2\uff1a\u963f\u4f1f\u4f60\u53c8\u5728\u6253\u7535\u52a8\u54e6\uff0c\u4f11\u606f\u4e00\u4e0b\u5427\uff0c\u770b\u4e2a\u4e66\u597d\u4e0d\u597d\uff1f",// 292
                "\u963f\u4f1f\uff1a\u70e6\u54ce\u3002",// 293
                "\u963f\u5af2\uff1a\u6211\u5728\u548c\u4f60\u8bf4\u8bdd\u4f60\u6709\u6ca1\u6709\u542c\u5230\uff1f",// 294
                "\u963f\u4f1f\uff1a\u4f60\u4e0d\u8981\u70e6\u597d\u4e0d\u597d\uff01",// 295
                "\u963f\u5af2\uff1a\u6211\u624d\u8bf4\u4f60\u4e24\u53e5\u4f60\u5c31\u8bf4\u6211\u70e6\uff01",// 296
                "\u963f\u5af2\uff1a\u6211\u53ea\u5e0c\u671b\u4f60\u80fd\u591f\u597d\u597d\u8bfb\u4e66\uff0c\u6574\u5929\u770b\u5230\u4f60\u5728\u90a3\u8fb9\u6253\u7535\u52a8\uff01",// 297
                "\u963f\u4f1f\uff1a\u554a\uff0c\u6b7b\u4e86\u5566\uff0c\u90fd\u4f60\u5bb3\u7684\u5566\uff01\u62dc\u6258\uff01",// 298
                "\u963f\u4f1f\uff1a\u90a3\u5929\uff0c\u6211\u53ea\u662f\u56e0\u4e3a\u53d7\u4e0d\u4e86\u963f\u5af2\u5570\u55e6\uff0c\u5c31\u51b2\u51fa\u53bb\uff0c\u8c01\u77e5\u9053\u7adf\u7136...",// 299
                "\u963f\u4f1f\uff1a\u4ed6\u8d85\u5e9f\u7684\u6b38\uff01",// 300
                "\u5f6c\u5f6c\uff1a\u6211\u77e5\u9053\u554a\uff0c\u4e4b\u524d\u5c31\u5e72\u6389\u4ed6\u8fc7",// 301
                "\u963f\u4f1f\uff1a\u4e8c\u73ed\u90a3\u4e2a\u554a\uff0c\u4ed6\u6bcf\u6b21\u90fd\u88ab\u6211\u6d17\u6218\u7ee9\u6b38\u3002",// 302
                "\u5f6c\u5f6c\uff1a\u4ed6\u5c31\u5ae9\u5457",// 303
                "\u963f\u4f1f\uff1a\u54ce\u5feb\u70b9\u90a3",// 304
                "\u5f6c\u5f6c\uff1a\u770b\u5230\u770b\u5230",// 305
                "\u963f\u4f1f\uff1a\u5de6\u8fb9\u5de6\u8fb9",// 306
                "\u5f6c\u5f6c\uff1a\u597d\u561e",// 307
                "\u963f\u4f1f\uff1a\u4e0a\u9762\u4e0a\u9762\u4e0a\u9762\u4e0a\u9762\u4e0a\u9762",// 308
                "\u5f6c\u5f6c\uff1a\u653e\u4e86\u653e\u4e86",// 309
                "\u963f\u4f1f\uff1a\u5de6\u8fb9\u5de6\u8fb9",// 310
                "\u5f6c\u5f6c\uff1a\u54e6\u4f60\u4e0d\u4f1a\u653e\u54e6\u3002\u5feb\u70b9\u5feb\u70b9\uff0c\u6211\u6b7b\u4e86\uff01",// 311
                "\u963f\u4f1f\uff1a\u590d\u6d3b\u590d\u6d3b",// 312
                "\u5f6c\u5f6c\uff1a\u6765\u6551\u6211\u5feb\u70b9\u5feb\u70b9",// 313
                "\u963f\u4f1f\uff1a\u6765\u4e86\u6765\u4e86\u6765\u4e86\u6765\u4e86",// 314
                "\u5f6c\u5f6c\uff1a\u554a\u6211\u6b7b\u4e86\u6211\u4e0d\u8981\u73a9\u4e86",// 315
                "\u963f\u4f1f\uff1a\u54ce",// 316
                "\u5f6c\u5f6c\uff1a\u5e72\u561b\u5566",// 317
                "\u963f\u4f1f\uff1a\u4e0d\u8981\u6bcf\u6b21\u90fd\u8fd9\u6837\u597d\u4e0d\u597d",// 318
                "\u5f6c\u5f6c\uff1a\u4e0d\u60f3\u73a9\u4e86\u5566",// 319
                "\u963f\u4f1f\uff1a\u54ce\u4f60\u8fd8\u6709\u6ca1\u6709\u94b1\u554a\u6211\u809a\u5b50\u597d\u997f",// 320
                "\u5f6c\u5f6c\uff1a\u6ca1\u4e86\u5566\u4eca\u5929\u90fd\u82b1\u5149\u4e86",// 321
                "\u963f\u4f1f\uff1a\u6211\u521a\u521a\u4e0d\u662f\u8ba9\u4f60\u591a\u5e26\u4e00\u70b9\u5417\uff1f",// 322
                "\u5f6c\u5f6c\uff1a\u4f60\u5e72\u561b\u81ea\u5df1\u4e0d\u591a\u5e26",// 323
                "\u963f\u4f1f\uff1a\u4f60\u4e0d\u8981\u6bcf\u6b21\u90fd\u8fd9\u6837\u597d\u4e0d\u597d",// 324
                "\u5f6c\u5f6c\uff1a\u597d\u4e86\u65f6\u95f4\u4e5f\u5dee\u4e0d\u591a\u4e86\u6211\u4eec\u8be5\u56de\u5bb6\u4e86",// 325
                "\u963f\u4f1f\uff1a\u4e0d\u8981\u6211\u8981\u518d\u73a9\u4e00\u4e0b",// 326
                "\u963f\u4f1f\uff1a\u54ce\u5f6c\u5f6c",// 327
                "\u5f6c\u5f6c\uff1a\u5e72\u561b",// 328
                "\u963f\u4f1f\uff1a\u6211\u597d\u997f\u54e6\uff0c\u6211\u4eec\u4e24\u4e2a\u90fd\u6ca1\u94b1\u4e86\uff0c\u4f60\u8981\u5e72\u561b\u554a",// 329
                "\u5f6c\u5f6c\uff1a\u6ca1\u6709\u94b1\u6211\u4eec\u5c31\u53ea\u80fd\u56de\u5bb6",// 330
                "\u963f\u4f1f\uff1a\u6211\u624d\u4e0d\u8981\u56de\u5bb6\u6b38\u6211\u963f\u5af2\u8d85\u51f6\u7684\uff0c\u53bb\u4f4f\u4f60\u5bb6\u5566",// 331
                "\u5f6c\u5f6c\uff1a\u4e0d\u884c\u5566",// 332
                "\u963f\u4f1f\uff1a\u4e3a\u4ec0\u4e48\u4e0d\u884c",// 333
                "\u5f6c\u5f6c\uff1a\u6211\u81ea\u5df1\u90fd\u81ea\u8eab\u96be\u4fdd\u4e86",// 334
                "\u963f\u4f1f\uff1a\u54ea\u6709",// 335
                "\u5f6c\u5f6c\uff1a\u800c\u4e14\u6211\u7238\u4f1a\u63cd\u6211",// 336
                "\u963f\u4f1f\uff1a\u771f\u7684\u5047\u7684",// 337
                "\u6770\u54e5\uff1a\u6b38\u4e0d\u597d\u610f\u601d\uff0c\u6211\u521a\u521a\u542c\u5230\u4f60\u4eec\u4e24\u4e2a\u8bf4\u809a\u5b50\u997f\uff0c\u6211\u8fd9\u521a\u597d\u6709\u4e2a\u9762\u5305",// 338
                "\u6770\u54e5\uff1a\u6211\u8fd8\u4e0d\u997f\uff0c\u6765\u8bf7\u4f60\u4eec\u5403",// 339
                "\u963f\u4f1f\uff1a\u5148\u5403\u5148\u5403",// 340
                "\u6770\u54e5\uff1a\u5bf9\u4e86\uff0c\u6211\u53eb\u963f\u6770\uff0c\u6211\u4e5f\u5e38\u6765\u8fd9\u91cc\u73a9\uff0c\u4ed6\u4eec\u90fd\u53eb\u6211\u6770\u54e5",// 341
                "\u963f\u4f1f\uff0c\u5f6c\u5f6c\uff1a\u6770\u54e5\u597d",// 342
                "\u963f\u4f1f\uff1a\u5148\u5403\u5566\u5148\u5403\u5566",// 343
                "\u6770\u54e5\uff1a\u4f60\u4eec\u597d\uff0c\u6211\u4e00\u4e2a\u4eba\u4f4f\uff0c\u6211\u7684\u623f\u5b50\u8fd8\u86ee\u5927\u7684",// 344
                "\u6770\u54e5\uff1a\u6b22\u8fce\u4f60\u4eec\u5230\u6211\u5bb6\u73a9\uff0c\u73a9\u7d2f\u4e86\u5c31\u76f4\u63a5\u7761\u89c9\uff0c\u6ca1\u95ee\u9898\u7684",// 345
                "\u963f\u4f1f\uff1a\u4f60\u89c9\u5f97\u5462",// 346
                "\u5f6c\u5f6c\uff1a\u6211\u89c9\u5f97\u602a\u602a\u7684",// 347
                "\u963f\u4f1f\uff1a\u5c31\u662f\u4e00\u4e2a\u5f88\u5947\u602a\u7684\u4eba\u554a\uff0c\u4e0d\u8981\u7406\u4ed6",// 348
                "\u5f6c\u5f6c\uff1a\u4e0d\u8981\u53bb\u4e0d\u8981\u53bb",// 349
                "\u6770\u54e5\uff1a\u6211\u5e38\u5e38\u5e2e\u52a9\u4e00\u4e9b\u7fd8\u5bb6\u7684\u4eba\uff0c\u5982\u679c\u4f60\u4eec\u4e0d\u8981\u6765\u7684\u8bdd\uff0c\u4e5f\u6ca1\u6709\u5173\u7cfb",// 350
                "\u6770\u54e5\uff1a\u5982\u679c\u8981\u6765\u7684\u8bdd\uff0c\u6211\u7b49\u4f1a\u53ef\u4ee5\u5e26\u4f60\u4eec\u53bb\u8d85\u5546\uff0c\u4e70\u4e00\u4e9b\u597d\u5403\u7684\u54e6",// 351
                "\u963f\u4f1f\uff1a\u6709\u4e1c\u897f\u53ef\u4ee5\u5403\u54ce\uff0c\u8981\u4e0d\u8981\u53bb\u554a",// 352
                "\u5f6c\u5f6c\uff1a\u597d\u4e86\u4e0d\u8981\u53bb",// 353
                "\u963f\u4f1f\uff1a\u53bb\u4e00\u4e0b\u597d\u4e86\u5566",// 354
                "\u5f6c\u5f6c\uff1a\u597d\u597d",// 355
                "\u963f\u4f1f\uff1a\u6770\u54e5\u90a3\u6211\u8ddf\u6211\u670b\u53cb\u4eca\u5929\u5c31\u4f4f\u4f60\u5bb6",// 356
                "\u6770\u54e5\uff1a\u597d\u554a\u6ca1\u95ee\u9898\u554a\uff0c\u90a3\u8d70\u554a\uff0c\u6211\u4eec\u73b0\u5728\u5c31\u53bb\u8d85\u5546\u4e70\u4e00\u4e9b\u5403\u7684",// 357
                "\u963f\u4f1f\uff1a\u597d\u554a",// 358
                "\u6770\u54e5\uff1a\u8d70\u8d70\u8d70\u8d70\u8d70",// 359
                "\u963f\u4f1f\uff1a\u4f60\u53bb\u90a3\u8fb9\u4f60\u53bb\u90a3\u8fb9",// 360
                "\u5f6c\u5f6c\uff1a\u6ce1\u9762",// 361
                "\u963f\u4f1f\uff1a\u5c0f\u6ce1\u8299\u6b38",// 362
                "\u6770\u54e5\uff1a\u90fd\u53ef\u4ee5\u62ff",// 363
                "\u963f\u4f1f\uff1a\u8c22\u8c22\u6770\u54e5",// 364
                "\u5f6c\u5f6c\uff1a\u597d\u591a\u996e\u6599\u54e6",// 365
                "\u963f\u4f1f\uff1a\u6709\u9152\u54ce",// 366
                "\u5f6c\u5f6c\uff1a\u4e0d\u8981\u770b\u9152\u5566\u5148\u770b\u996e\u6599",// 367
                "\u5f6c\u5f6c\uff1a\u4ec0\u4e48\u90fd\u53ef\u4ee5\u62ff\u5417",// 368
                "\u6770\u54e5\uff1a\u90fd\u53ef\u4ee5\u62ff",// 369
                "\u5f6c\u5f6c\uff1a\u771f\u7684\u5047\u7684",// 370
                "\u6770\u54e5\uff1a\u968f\u4fbf\u62ff\uff0c\u4f60\u4eec\u968f\u4fbf\u62ff",// 371
                "\u963f\u4f1f\uff1a\u771f\u7684\u53ef\u4ee5\u5417",// 372
                "\u6770\u54e5\uff1a\u53ef\u4ee5\u62ff\uff0c\u90fd\u62ff",// 373
                "\u963f\u4f1f\uff1a\u8c22\u8c22\u6770\u54e5",// 374
                "\u5f6c\u5f6c\uff1a\u554a\u518d\u559d\uff0c\u518d\u559d\uff0c\u554a\u518d\u559d\u518d\u559d",// 375
                "\u6770\u54e5\uff1a\u4f60\u770b\uff0c\u4f60\u770b\u90a3\u4e2a\u5f6c\u5f6c\uff0c\u624d\u559d\u51e0\u7f50\u5c31\u9189\u4e86\uff0c\u771f\u7684\u592a\u900a\u4e86",// 376
                "\u963f\u4f1f\uff1a\u8fd9\u4e2a\u5f6c\u5f6c\u5c31\u662f\u900a\u5566",// 377
                "\u6770\u54e5\uff1a\u542c\u4f60\u90a3\u4e48\u8bf4\uff0c\u4f60\u5f88\u52c7\u54e6",// 378
                "\u963f\u4f1f\uff1a\u5f00\u73a9\u7b11\uff0c\u6211\u8d85\u52c7\u7684\u597d\u4e0d\u597d\uff0c\u6211\u8d85\u4f1a\u559d\u7684\u5566",// 379
                "\u6770\u54e5\uff1a\u8d85\u4f1a\u559d\uff0c\u5f88\u52c7\u561b\uff0c\u8eab\u6750\u4e0d\u9519\u54e6\uff0c\u86ee\u7ed3\u5b9e\u7684\u554a",// 380
                "\u5f6c\u5f6c\uff1a\u6770\u54e5\u4f60\u5e72\u561b\u554a",// 381
                "\u6770\u54e5\uff1a\u90fd\u51e0\u5c81\u4e86\uff0c\u8fd8\u90a3\u4e48\u5bb3\u7f9e\uff0c\u6211\u770b\u4f60\u5b8c\u5168\u662f\u4e0d\u61c2\u54e6",// 382
                "\u963f\u4f1f\uff1a\u61c2\u4ec0\u4e48\u554a",// 383
                "\u6770\u54e5\uff1a\u4f60\u60f3\u61c2\uff0c\u6211\u623f\u91cc\u6709\u4e00\u4e9b\u597d\u5eb7\u7684",// 384
                "\u963f\u4f1f\uff1a\u597d\u5eb7\uff0c\u662f\u65b0\u6e38\u620f\u54e6",// 385
                "\u6770\u54e5\uff1a\u4ec0\u4e48\u65b0\u6e38\u620f\uff0c\u6bd4\u6e38\u620f\u8fd8\u523a\u6fc0\uff0c\u8fd8\u53ef\u4ee5\u6559\u4f60\u767bdua\u90ce\u54e6",// 386
                "\u963f\u4f1f\uff1a\u767bdua\u90ce\uff1f",// 387
                "\u6770\u54e5\uff1a\u5bf9\u4e86\u6765\u770b\u4f60\u5c31\u77e5\u9053\u4e86",// 388
                "\u963f\u4f1f\uff1a\u6770\u54e5\u9152",// 389
                "\u6770\u54e5\uff1a\u62ff\u62ff\u62ff\u6765\u6765\u6765",// 390
                "\u963f\u4f1f\uff1a\u5e72\u561b\u5566",// 391
                "\u963f\u4f1f\uff1a\u6770\u54e5\u4f60\u6709\u597d\u591aA\u7247\u54e6",// 392
                "\u6770\u54e5\uff1a\u90a3\u6ca1\u4ec0\u4e48\u6765\u770b\u8fd9\u4e2a\u597d\u5eb7\u7684",// 393
                "\u963f\u4f1f\uff1a\u6770\u54e5\uff0c\u8fd9\u662f\u4ec0\u4e48\u554a",// 394
                "\u6770\u54e5\uff1a\u54ce\u5466\uff0c\u4f60\u8138\u7ea2\u5566\uff0c\u6765\uff0c\u8ba9\u6211\u770b\u770b",// 395
                "\u963f\u4f1f\uff1a\u4e0d\u8981\u5566",// 396
                "\u6770\u54e5\uff1a\u8ba9\u6211\u770b\u770b",// 397
                "\u963f\u4f1f\uff1a\u4e0d\u8981\u5566\uff0c\u6770\u54e5\uff0c\u4f60\u5e72\u561b\u554a",// 398
                "\u6770\u54e5\uff1a\u8ba9\u6211\u770b\u770b\u4f60\u53d1\u80b2\u6b63\u4e0d\u6b63\u5e38\u554a",// 399
                "\u963f\u4f1f\uff1a\u6770\u54e5\uff0c\u4e0d\u8981\u5566",// 400
                "\u6770\u54e5\uff1a\u542c\u8bdd\uff0c\u8ba9\u6211\u770b\u770b\uff01\uff01",// 401
                "\u963f\u4f1f\uff1a\u4e0d\u8981",// 402
                "\u963f\u4f1f\uff1a\u6770\u54e5\u4e0d\u8981\u554a\uff0c\u6770\u54e5\u4e0d\u8981\uff0c\u6770\u54e5",// 403
                "\u6770\u54e5\uff1a\u8fd9\u4ef6\u4e8b\u662f\u6211\u4eec\u4e24\u4e2a\u4eba\u4e4b\u95f4\u7684\u79d8\u5bc6\uff0c\u4f60\u6700\u597d\u4e0d\u8981\u7ed9\u6211\u544a\u8bc9\u4efb\u4f55\u4eba",// 404
                "\u6770\u54e5\uff1a\u5982\u679c\u4f60\u8981\u8bf4\u51fa\u53bb\uff0c\u5c31\u7ed9\u6211\u5c0f\u5fc3\u4e00\u70b9",// 405
                "\u6770\u54e5\uff1a\u6211\u77e5\u9053\u4f60\u5b66\u6821\u5728\u54ea\uff0c\u4e5f\u77e5\u9053\u4f60\u90fd\u90a3\u4e00\u73ed",// 406
                "\u6770\u54e5\uff1a\u4f60\u6700\u597d\u7ed9\u6211\u597d\u597d\u8bb0\u4f4f\uff0c\u61c2\u5417",// 407
                "\u5b8c"// 408
        };
        this.jieGeList = CollectionsKt.arrayListOf(files);// 289 290
        files = new String[]{
                "<\u4f60\u770b\u5230\u7684\u6211>",
                "\u80cc\u8d77\u4e86\u884c\u56ca",// 412
                "\u79bb\u5f00\u5bb6\u7684\u90a3\u4e00\u523b",// 413
                "\u6211\u77e5\u9053\u73b0\u5b9e\u751f\u6d3b",// 414
                "\u6709\u592a\u591a\u7279\u522b\u7684\u7279",// 415
                "\u5047\u5982\u4f60\u770b\u5230\u4e86\u6211",// 416
                "\u4e5f\u4e0d\u8981\u592a\u8fc7\u51b7\u6f20",// 417
                "\u6211\u591a\u6101\u5584\u611f",// 418
                "\u4f46\u4e5f\u70ed\u60c5\u5954\u653e\u6d12\u8131",// 419
                "\u5306\u5fd9\u7684\u4e16\u754c",// 421
                "\u6765\u4e0d\u53ca\u95ee\u4e3a\u4ec0\u4e48",// 422
                "\u6211\u77e5\u9053\u6f02\u6cca\u7684\u4eba",// 423
                "\u5fc3\u4e2d\u90fd\u6709\u4e00\u56e2\u706b",// 424
                "\u5047\u5982\u4f60\u53c8\u770b\u5230\u6211",// 425
                "\u8bf7\u7ed9\u6211\u4e00\u4e2a\u62e5\u62b1",// 426
                "\u6211\u5076\u5c14\u6c89\u9ed8\u4f46\u4e5f\u52c7\u6562\u6267\u7740",// 427
                "\u4f60\u770b\u5230\u7684\u6211\u4f60\u770b\u5230\u7684\u6211",// 428
                "\u662f\u54ea\u4e00\u79cd\u989c\u8272\u60b2\u4f24\u6216\u5feb\u4e50",// 429
                "\u4e5f\u8bb8\u8001\u4e86\u4e00\u70b9",// 430
                "\u773c\u795e\u53d8\u5f97\u4e0d\u518d\u90a3\u4e48\u6e05\u6f88",// 431
                "\u70ed\u8840\u4f9d\u7136\u6cb8\u817e\u7740\u6211\u7684\u8109\u640f",// 432
                "\u4f60\u770b\u5230\u7684\u6211",// 433
                "\u4f60\u770b\u5230\u7684\u6211",// 434
                "\u662f\u54ea\u4e00\u79cd\u6027\u683c",// 435
                "\u5f00\u6717\u6216\u6162\u70ed",// 436
                "\u50cf\u52c7\u6562\u7684\u96c4\u9e70",// 437
                "\u4e0d\u6015\u98ce\u96e8\u4e0d\u6015\u56f0\u96be\u632b\u6298",// 438
                "\u5982\u679c\u8981\u98de\u5c31\u98de\u51fa\u5929\u7a7a\u6d77\u9614",// 439
                "\u6211\u5c31\u662f\u6211",// 440
                "\u5306\u5fd9\u7684\u4e16\u754c\u6765\u4e0d\u53ca\u95ee\u4e3a\u4ec0\u4e48",// 442
                "\u6211\u77e5\u9053\u6f02\u6cca\u7684\u4eba",// 443
                "\u5fc3\u4e2d\u90fd\u6709\u4e00\u56e2\u706b",// 444
                "\u5047\u5982\u4f60\u53c8\u770b\u5230\u6211",// 445
                "\u8bf7\u7ed9\u6211\u4e00\u4e2a\u62e5\u62b1",// 446
                "\u6211\u5076\u5c14\u6c89\u9ed8\u4f46\u4e5f\u52c7\u6562\u6267\u7740",// 447
                "\u4f60\u770b\u5230\u7684\u6211\u4f60\u770b\u5230\u7684\u6211",// 448
                "\u662f\u54ea\u4e00\u79cd\u989c\u8272\u60b2\u4f24\u6216\u5feb\u4e50",// 449
                "\u4e5f\u8bb8\u8001\u4e86\u4e00\u70b9",// 450
                "\u773c\u795e\u53d8\u5f97\u4e0d\u518d\u90a3\u4e48\u6e05\u6f88",// 451
                "\u70ed\u8840\u4f9d\u7136\u6cb8\u817e\u7740\u6211\u7684\u8109\u640f",// 452
                "\u4f60\u770b\u5230\u7684\u6211\u4f60\u770b\u5230\u7684\u6211",// 453
                "\u662f\u54ea\u4e00\u79cd\u6027\u683c",// 454
                "\u5f00\u6717\u6216\u6162\u70ed",// 455
                "\u50cf\u52c7\u6562\u7684\u96c4\u9e70",// 456
                "\u4e0d\u6015\u98ce\u96e8\u4e0d\u6015\u56f0\u96be\u632b\u6298",// 457
                "\u5982\u679c\u8981\u98de\u5c31\u98de\u51fa\u5929\u7a7a\u6d77\u9614",// 458
                "\u6211\u5c31\u662f\u6211",// 459
                "\u4f60\u770b\u5230\u7684\u6211\u4f60\u770b\u5230\u7684\u6211",// 461
                "\u662f\u54ea\u4e00\u79cd\u989c\u8272\u60b2\u4f24\u6216\u5feb\u4e50",// 462
                "\u4e5f\u8bb8\u8001\u4e86\u4e00\u70b9",// 463
                "\u773c\u795e\u53d8\u5f97\u4e0d\u518d\u90a3\u4e48\u6e05\u6f88",// 464
                "\u70ed\u8840\u4f9d\u7136\u6cb8\u817e\u7740\u6211\u7684\u8109\u640f",// 465
                "\u4f60\u770b\u5230\u7684\u6211\u4f60\u770b\u5230\u7684\u6211",// 466
                "\u662f\u54ea\u4e00\u79cd\u6027\u683c",// 467
                "\u5f00\u6717\u6216\u6162\u70ed",// 468
                "\u50cf\u52c7\u6562\u7684\u96c4\u9e70",// 469
                "\u4e0d\u6015\u98ce\u96e8\u4e0d\u6015\u56f0\u96be\u632b\u6298",// 470
                "\u5982\u679c\u8981\u98de\u5c31\u98de\u51fa\u5929\u7a7a\u6d77\u9614",// 471
                "\u6211\u5c31\u662f\u6211"// 472
        };
        this.niKanDaoDeWoList = CollectionsKt.arrayListOf(files);// 410 411
        files = new String[]{
                "<AlieZ>",
                "\u6c7a\u3081\u3064\u3051\u3070\u304b\u308a",// 476
                "\u81ea\u60da\u308c\u3092\u7740\u305f\u30c1\u30fc\u30d7\u306a hokori \u3067",// 477
                "\u97f3\u8352\u3052\u3066\u3082",// 478
                "\u68da\u306b\u96a0\u3057\u305f\u54c0\u308c\u306a",// 479
                "\u6065\u306b\u6fe1\u308c\u305f\u93e1\u306e\u4e2d",// 480
                "\u90fd\u5408\u306e\u50b7\u3060\u3051\u3072\u3051\u3089\u304b\u3057\u3066",// 481
                "\u624b\u8efd\u306a\u5f37\u3055\u3067\u52dd\u53d6\u308b\u8853\u3092",// 482
                "\u3069\u308c\u3060\u3051\u78e8\u3044\u3067\u3082\u6c17\u306f\u3084\u3064\u308c\u308b",// 483
                "\u3075\u3089\u3064\u3044\u305f\u601d\u60f3\u901a\u308a\u3060",// 484
                "\u611b-same-CRIER \u611b\u64ab-save-LIAR",// 485
                "Eid-\u8056-Rising HELL",// 486
                "\u611b\u3057\u3066\u308b game \u4e16\u754c\u306e day",// 487
                "Don't-\u751f-War Lie-\u5175\u58eb-War-World",// 488
                "Eyes-Hate-War",// 489
                "A-Z Looser-Krankheit-Was IS das?",// 490
                "\u53d7\u3051\u58f2\u308a\u76fe\u306b\u898b\u4e0b\u3057\u3066\u3066\u3082",// 491
                "\u305d\u3053\u306b\u306f\u5730\u9762\u3057\u304b\u306a\u3044\u4e8b\u3055\u3048",// 492
                "\u6c17\u4ed8\u304b\u306c\u307e\u307e\u306b\u58ca\u308c\u305f",// 493
                "\u904e\u53bb\u306b\u8ca0\u3051\u305f\u93e1\u306e\u5965",// 494
                "\u3069\u3053\u307e\u3067\u53eb\u3079\u3070\u4f4d\u7f6e\u3092\u77e5\u308c\u308b",// 495
                "\u3068\u3069\u3081\u3082\u306a\u3044\u307e\u307e\u606f\u304c\u5207\u308c\u308b",// 496
                "\u5802\u3005\u3055\u3089\u3057\u305f\u7f6a\u306e\u7fa4\u308c\u3068",// 497
                "\u5f8c\u308d\u5411\u304d\u306b\u3042\u3089\u304c\u3046",// 498
                "\u611b-same-CRIER \u611b\u64ab-save-LIAR",// 499
                "Aid-\u8056-Rising HELL",// 500
                "I'll-ness Reset-End\u3058\u3083\u306a\u3044 Burst",// 501
                "Don't-\u751f-War Lie-\u5175\u58eb-War-World",// 502
                "Eyes-Hate-War",// 503
                "A-Z \u60f3\u50cfHigh-de-Siehst YOU das?",// 504
                "\u507d\u306e\u614b\u5ea6\u306a\u81c6\u75c5loud voice",// 505
                "\u6c17\u9ad8\u3055\u3092\u52d8\u9055\u3044\u3057\u305f\u5fc3\u81d3\u97f3",// 506
                "\u72d9\u3044\u901a\u308a\u306e\u5e7b\u898b\u3066\u3082",// 507
                "\u6e80\u305f\u305b\u306a\u3044\u4f55\u5ea6\u3082\u76ee\u3092\u958b\u3051\u3066\u3082",// 508
                "\u3069\u3053\u307e\u3067\u53eb\u3079\u3070\u4f4d\u7f6e\u3092\u77e5\u308c\u308b",// 509
                "\u3068\u3069\u3081\u3082\u306a\u3044\u307e\u307e\u606f\u304c\u5207\u308c\u308b",// 510
                "\u5802\u3005\u3055\u3089\u3057\u305f\u7f6a\u306e\u7fa4\u308c\u3068",// 511
                "\u5f8c\u308d\u5411\u304d\u306b\u3042\u3089\u304c\u3046",// 512
                "\u611b-same-CRIER \u611b\u64ab-save-LIAR",// 513
                "Eid-\u8056-Rising HELL",// 514
                "\u611b\u3057\u3066\u308b Game\u4e16\u754c\u306eDay",// 515
                "Don't-\u751f-War Lie-\u5175\u58eb-War-World",// 516
                "Eyes-Hate-War",// 517
                "A-Z Looser-Krankheit-Was IS das?",// 518
                "Leben was ist das?",// 519
                "Signal siehst du das?",// 520
                "Rade die du nicht weisst",// 521
                "Aus eigenem willen",// 522
                "Leben was ist das?",// 523
                "Signal siehst du das?",// 524
                "Rade die du nicht weisst",// 525
                "Sieh mit deinen augen"// 526
        };
        this.aliezList = CollectionsKt.arrayListOf(files);// 474 475
        files = new String[]{
                "<\u8bf4\u53e5\u5b9e\u8bdd>",
                "\u6211\u8fd8\u662f\u4e60\u60ef\u6027\u7684\u5bf9\u4f60\u597d\u5947",// 530
                "\u7aa5\u63a2\u4f60\u7684\u751f\u6d3b",// 531
                "\u4f46\u653e\u4e0b\u6240\u6709\u4e00\u5207\u5173\u4e8e\u4f60\u7684\u4e0d\u518d\u4e89\u593a",// 532
                "\u6211\u65e0\u6240\u8c13\u53bb\u63a5\u53d7\u4f60\u7684 runaway",// 533
                "\u53cd\u6b63\u4f60\u73b0\u5728\u7684\u4ed6\u6ca1\u6211 OK",// 534
                "\u8bf4\u53e5\u5b9e\u8bdd",// 535
                "Who can do it like me",// 536
                "Who can do it like me",// 537
                "\u8bf4\u53e5\u5b9e\u8bdd",// 538
                "Who can do it like me",// 539
                "Who can do it like me",// 540
                "\u8bf4\u53e5\u5b9e\u8bdd",// 541
                "Who can do it like me",// 542
                "Who can do it like me",// 543
                "\u8bf4\u53e5\u5b9e\u8bdd",// 544
                "Who can do it like me",// 545
                "Who can do it like me",// 546
                "\u5f53\u4f60\u5f00\u59cb\u5bf9\u6211\u5931\u671b\u4e4b\u540e",// 547
                "\u6211\u7684\u521b\u4f5c\u90fd\u9760\u9152\u7cbe",// 548
                "\u8ba9\u66b4\u96e8\u628a\u6211\u6dcb\u7684\u6e7f\u900f",// 549
                "\u4ed6\u4eec\u8bf4\u6211\u5f00\u59cb\u53d8\u7684\u8d70\u5fc3",// 550
                "U know girl \u73b0\u5728\u7684\u6211\u6ef4\u9152\u4e0d\u6cbe",// 551
                "\u5f53\u6211\u5f7b\u5e95\u7684\u4e0d\u5728\u4e4e\u624d\u53d1\u73b0",// 552
                "\u8eab\u8fb9\u7684\u5973\u5b69\u90fd\u6bd4\u4f60\u597d\u770b",// 553
                "\u5f53\u521d\u53d1\u8fc7\u8a93\u4e00\u5b9a\u628a\u4f60\u8ffd\u56de\u6765",// 554
                "\u6211\u627f\u8ba4\u540e\u6765\u88ab\u96f7\u5288\u4e86",// 555
                "\u66fe\u7ecf\u80fd\u80cc\u4e0b\u7535\u8bdd\u91cc\u9762\u7684\u5bf9\u767d",// 556
                "\u73b0\u5728\u8fde\u53f7\u7801\u90fd\u4e0d\u8bb0\u5f97",// 557
                "\u4f46\u6211\u60f3\u9080\u8bf7\u4f60\u770b\u4e00\u4e0b\u6211\u7684\u73b0\u5728",// 558
                "\u623f\u95f4\u91cc\u968f\u4fbf\u5750\u522b\u518d\u89c1\u5916",// 559
                "\u4e00\u8d77\u517b\u8fc7\u7684\u72d7\u4f9d\u7136\u5065\u5728",// 560
                "\u6211\u627f\u8ba4\u6211\u66fe\u7ecf\u5c31\u50cf\u4e2a\u53d8\u6001",// 561
                "Who can do it like me what will I be",// 562
                "\u592a\u591a\u60f3\u4e0d\u51fa\u7b54\u6848\u7684\u95ee\u9898",// 563
                "\u8fde\u6211\u81ea\u5df1\u90fd\u5f00\u59cb\u6000\u7591",// 564
                "Who can do it like me",// 565
                "\u5c31\u50cf\u80ce\u8bb0",// 566
                "\u50cf\u662f\u4e0e\u751f\u4ff1\u6765\u7684\u4e00\u76f4\u5b58\u5728\u7684",// 567
                "\u90fd\u4e0d\u501f\u52a9\u5916\u529b",// 568
                "\u6211\u8fd8\u662f\u4e60\u60ef\u6027\u7684\u5bf9\u4f60\u597d\u5947",// 569
                "\u7aa5\u63a2\u4f60\u7684\u751f\u6d3b",// 570
                "\u4f46\u653e\u4e0b\u6240\u6709\u4e00\u5207\u5173\u4e8e\u4f60\u7684\u4e0d\u518d\u4e89\u593a",// 571
                "\u6211\u65e0\u6240\u8c13\u53bb\u63a5\u53d7\u4f60\u7684 runaway",// 572
                "\u53cd\u6b63\u4f60\u73b0\u5728\u7684\u4ed6\u6ca1\u6211 OK",// 573
                "\u8bf4\u53e5\u5b9e\u8bdd",// 574
                "Who can do it like me",// 575
                "Who can do it like me",// 576
                "\u8bf4\u53e5\u5b9e\u8bdd",// 577
                "Who can do it like me",// 578
                "Who can do it like me",// 579
                "\u8bf4\u53e5\u5b9e\u8bdd",// 580
                "Who can do it like me",// 581
                "Who can do it like me",// 582
                "\u8bf4\u53e5\u5b9e\u8bdd",// 583
                "Who can do it like me",// 584
                "Who can do it like me",// 585
                "\u5728\u975e\u6d32 run \uff12 \u957f\u9888\u9a6c",// 586
                "\u8ba9\u66b4\u96e8\u6dcb\u5230\u4e86\u5723\u8bde\u8282",// 587
                "\u5728\u9003\u547d\u524d\u62b1\u597d\u4f60\u9ec4\u91d1\u5427",// 588
                "Jet \u5f0f\u75c5\u6bd2\u90fd\u4e0d\u7528\u8513\u5ef6",// 589
                "\u8fd9\u9996\u6b4c\u53ea\u662f\u8868\u8fbe\u6211\u725b\u903c",// 590
                "\u4f60\u6709\u4ec0\u4e48\u610f\u89c1\u5462",// 591
                "\u4e00\u811a\u628a\u4f60\u8e39\u98de\u6eda\u4e0b\u697c\u68af",// 592
                "Peace and love",// 593
                "\u6211\u8fd9\u4e48\u72c2\u91ce\u6ca1\u6709\u8c26\u865a\u4ece\u4e0d be humble",// 594
                "Make sh*t \u811a\u8e0f\u5b9e\u5730 \u7edd\u4e0d\u662f\u5145\u6c14\u7684",// 595
                "U wanna do it like me \u79d8\u8bc0\u662f dgu",// 596
                "\u8bf4\u53e5\u5b9e\u8bdd",// 597
                "Who can do it like me",// 598
                "Who can do it like me",// 599
                "\u8bf4\u53e5\u5b9e\u8bdd",// 600
                "Who can do it like me",// 601
                "Who can do it like me",// 602
                "\u8bf4\u53e5\u5b9e\u8bdd",// 603
                "Who can do it like me",// 604
                "Who can do it like me",// 605
                "\u8bf4\u53e5\u5b9e\u8bdd",// 606
                "Who can do it like me",// 607
                "Who can do it like me"// 608
        };
        this.shuoJuShiHuaList = CollectionsKt.arrayListOf(files);// 528 529
        files = new String[]{
                "<Invincible>",
                "I feel like a super woman in your eyes tonight",// 612
                "And you make me feel like I am bulletproof inside",// 613
                "'Cause I'll fight for you give my life for you",// 614
                "And I got you by my side",// 615
                "There's no barricade we can't tear away",// 616
                "When it comes to you and I",// 617
                "'Cause even if we break even if we fall",// 618
                "Baby you know we can have it all",// 619
                "And if they knock us down like a wrecking ball",// 620
                "We'll get up and walk right through these walls",// 621
                "'Cause we are we are invincible invincible",// 622
                "We are we are invincible invincible",// 623
                "We are we are invincible invincible",// 624
                "We are we are invincible invincible",// 625
                "You make me feel not afraid of anything",// 626
                "And nothing in the universe will come between",// 627
                "'Cause I'll fight for you give my life for you",// 628
                "And I got you by my side",// 629
                "There's no barricade we can't tear away",// 630
                "When it comes to you and I",// 631
                "'Cause even if we break even if we fall",// 632
                "Baby you know we can have it all",// 633
                "And if they knock us down like a wrecking ball",// 634
                "We'll get up and walk right through these walls",// 635
                "'Cause we are we are invincible invincible",// 636
                "We are we are invincible invincible",// 637
                "We are we are invincible invincible",// 638
                "We are we are invincible invincible",// 639
                "I feel like a super woman in your eyes tonight",// 640
                "And you make me feel like I am bulletproof inside",// 641
                "'Cause I'll fight for you give my life for you",// 642
                "And I got you by my side",// 643
                "There's no barricade we can't tear away",// 644
                "When it comes to you and I",// 645
                "'Cause even if we break even if we fall",// 646
                "Baby you know we can have it all",// 647
                "And if they knock us down like a wrecking ball",// 648
                "We'll get up and walk right through these walls",// 649
                "'Cause we are we are invincible invincible",// 650
                "We are we are invincible invincible",// 651
                "We are we are invincible invincible",// 652
                "We are we are invincible invincible"// 653
        };
        this.invincibleList = CollectionsKt.arrayListOf(files);// 610 611
    }// 30

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        String mode;
        String var10000;
        label136: {
            Intrinsics.checkNotNullParameter(event, "event");
            mode = (String)this.modeValue.get();// 160
            String first = (String)this.startMode.get();
            switch (first.hashCode()) {
                case 33:
                    if (first.equals("!")) {
                        var10000 = "!";// 165
                        break label136;
                    }
                    break;
                case 46:
                    if (first.equals(".")) {
                        var10000 = ".say .";// 163
                        break label136;
                    }
                    break;
                case 64:
                    if (first.equals("@")) {
                        var10000 = "@";// 164
                        break label136;
                    }
                    break;
                case 1454983690:
                    if (first.equals("/shout")) {
                        var10000 = "/shout ";// 162
                        break label136;
                    }
                    break;
                case 2029746065:
                    if (first.equals("Custom")) {
                        var10000 = (String)this.customPrefix.get();// 166
                        break label136;
                    }
            }

            var10000 = "";// 167
        }

        String start = var10000;// 161
        String first = this.randomCharacterAtFirst.get()// 170
                ? start + (String)this.firstLeft.get() + RandomUtils.randomString(this.firstLength) + (String)this.firstRight.get()// 171
                : start;// 172
        String last = this.randomCharacterAtLast.get()// 173
                ? (String)this.lastLeft.get() + RandomUtils.randomString(this.lastLength) + (String)this.lastRight.get()// 174
                : "";// 175
        if (this.msTimer.hasTimePassed(this.delay)) {// 176
            if (StringsKt.equals(mode, "Switch", true)) {// 177
                byte var24;
                if (this.switchState == 1) {
                    String text = first + (String)this.switchMessage1.get() + last;// 179
                    ClientModule.mc.thePlayer.sendChatMessage(text);// 180
                    var24 = 2;// 181
                } else {
                    String text = first + (String)this.switchMessage2.get() + last;// 183
                    ClientModule.mc.thePlayer.sendChatMessage(text);// 184
                    var24 = 1;// 185
                }

                this.switchState = var24;// 178
            } else if (StringsKt.equals(mode, "Single", true)) {// 187
                String text = first + (String)this.messageValue.get() + last;// 188
                ClientModule.mc.thePlayer.sendChatMessage(text);// 189
            } else {
                ArrayList<String> spammerList;
                label140: {
                    String[] var7 = new String[]{""};// 191
                    spammerList = CollectionsKt.arrayListOf(var7);
                    Locale var20 = Locale.getDefault();

                    var10000 = mode.toLowerCase(var20);

                    String var18 = var10000;// 192
                    switch (var18.hashCode()) {
                        case -1779823950:
                            if (var18.equals("\u4f60\u770b\u5230\u7684\u6211")) {
                                spammerList = this.niKanDaoDeWoList;// 196
                                break label140;
                            }
                            break;
                        case -449934194:
                            if (var18.equals("\u7cbe\u901a\u4eba\u6027\u7684\u5973\u8bb2\u5e08")) {
                                spammerList = this.nvJiangShiList;// 194
                                break label140;
                            }
                            break;
                        case 92903123:
                            if (var18.equals("aliez")) {
                                spammerList = this.aliezList;// 197
                                break label140;
                            }
                            break;
                        case 659402520:
                            if (var18.equals("\u534e\u5f3a\u4e70\u74dc")) {
                                spammerList = this.huaQiangList;// 193
                                break label140;
                            }
                            break;
                        case 810405705:
                            if (var18.equals("\u6770\u54e5\u4e0d\u8981")) {
                                spammerList = this.jieGeList;// 195
                                break label140;
                            }
                            break;
                        case 1025079327:
                            if (var18.equals("invincible")) {
                                spammerList = this.invincibleList;// 199
                                break label140;
                            }
                            break;
                        case 1088754224:
                            if (var18.equals("\u8bf4\u53e5\u5b9e\u8bdd")) {
                                spammerList = this.shuoJuShiHuaList;// 198
                                break label140;
                            }
                    }

                    File[] var22 = KevinClient.INSTANCE.getFileManager().spammerDir.listFiles();// 201
                    Intrinsics.checkNotNull(var22);
                    File[] files = var22;
                    File file = null;// 202
                    int var10 = 0;

                    for (int var11 = files.length; var10 < var11; var10++) {// 203
                        File i = files[var10];
                        String var10001 = i.getName();
                        Intrinsics.checkNotNullExpressionValue(var10001, "getName(...)");
                        CharSequence var23 = (CharSequence)var10001;
                        String[] var13 = new String[]{".txt"};
                        if (Intrinsics.areEqual(mode, StringsKt.split(var23, var13, false, 0).get(0))) {// 204
                            file = i;
                        }
                    }

                    if (file != null) {// 206
                        spammerList = new ArrayList<String>(FilesKt.readLines(file, Charset.defaultCharset()));
                    }
                }

                if (!Intrinsics.areEqual(mode, this.lastMode)) {// 209
                    this.sentencesNumber = 0;// 210
                    this.lastMode = mode;// 211
                }

                if (!((Collection)spammerList).isEmpty()) {// 213
                    ClientModule.mc
                            .thePlayer
                            .sendChatMessage(
                                    this.customNoRandomV.get()
                                            ? start + (String)spammerList.get(this.sentencesNumber)
                                            : first + (String)spammerList.get(this.sentencesNumber) + last
                            );
                }

                if (this.sentencesNumber < spammerList.size() - 1) {
                    this.sentencesNumber++;
                } else if (this.autoDisableV.get()) {
                    ClientModule var19 = KevinClient.INSTANCE.getModuleManager().getModuleByName(this.getName());// 215
                    if (var19 != null) {
                        var19.toggle();// 214 216
                    }
                } else {
                    this.sentencesNumber = 0;
                }
            }

            this.msTimer.reset();// 218
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());// 219
            this.firstLength = RandomUtils.nextInt(((Number)this.firstMinLength.get()).intValue(), ((Number)this.firstMaxLength.get()).intValue());// 220 221
            this.lastLength = RandomUtils.nextInt(((Number)this.lastMinLength.get()).intValue(), ((Number)this.lastMaxLength.get()).intValue());// 222 223
        }
    }// 225

    public void onEnable() {
        this.lastMode = (String)this.modeValue.get();// 228
        this.msTimer.reset();// 229
        this.sentencesNumber = 0;// 230
    }// 231

    public void onDisable() {
        this.lastMode = null;// 234
        this.sentencesNumber = 0;// 235
    }// 236

    @NotNull
    public String getTag() {
        return (String)this.modeValue.get() + "  " + this.sentencesNumber;// 239
    }

    private static final boolean _init_$lambda$0(SuperSpammer this$0, @NotNull File it) {
        String var10000 = it.getName();// 52
        return StringsKt.endsWith(var10000, this$0.fileSuffix, false);
    }
}

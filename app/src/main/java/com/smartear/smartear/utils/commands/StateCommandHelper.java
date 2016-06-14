package com.smartear.smartear.utils.commands;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.smartear.smartear.wechat.RecognizedState;
import com.smartear.smartear.wechat.bus.VoiceCommandEvent;

import org.greenrobot.eventbus.EventBus;

public class StateCommandHelper extends BaseCommandHelper {

    private static final VoiceCommand[] voiceCommands = new VoiceCommand[]{
            new VoiceCommand(new String[]{"weather", "feather", "grether", "heather", "leather", "nether", "raether", "sether", "tether", "wether", "whether"}, RecognizedState.WEATHER),
            new VoiceCommand(new String[]{"morning", "corning", "corn ung", "horning", "hornung", "horn hung", "morn ing", "mourning", "scorning", "warning"}, RecognizedState.AUTH),
            new VoiceCommand(new String[]{"meeting", "beating", "bleating", "cheating", "creat ing", "crea ting", "eat ing", "feet hung", "fleeting", "geeting", "greeting", "heating", "heat hung", "heat ing", "keating", "lee ting", "lee tung", "li ting", "li tung", "meat hung", "meet ing", "meting", "pete hung", "pleating", "seating", "seat hung", "sheeting", "sheet hung", "shih tung", "sleeting", "smeeting", "sweeting", "sweet tongue", "treating", "tse tung", "tweeting", "wieting", "ye ting"}, RecognizedState.MEETING),
            new VoiceCommand(new String[]{"voice", "boice", "boyce", "choice", "choyce", "joice", "joyce", "loyce", "moyse", "noyce", "royce", "royse"}, RecognizedState.VOICE_MESSAGE),
            new VoiceCommand(new String[]{"play", "okay", "bay", "baye", "bayh", "bey", "blay", "bley", "brae", "bray", "brey", "cay", "che", "chez", "clay", "craie", "cray", "dae", "day", "daye", "dey", "dray", "drey", "dreye", "fay", "faye", "fe", "fey", "flay", "fleigh", "fray", "frey", "fs j.", "fs may", "gai", "gay", "gaye", "gray", "graye", "grey", "guay", "haigh", "hay", "haye", "heigh", "hey", "hwe", "j", "j.", "jae", "jay", "jaye", "k", "k.", "kay", "kaye", "klay", "kley", "kray", "krey", "laigh", "lait", "lay", "laye", "lei", "ley", "leyh", "mae", "may", "maye", "mey", "nay", "neigh", "nej", "ney", "pay", "paye", "pei", "pray", "prey", "quai", "quaigh", "quay", "quaye", "rae", "ray", "raye", "re", "rea", "reay", "rey", "say", "saye", "schey", "schley", "seay", "shay", "shea", "skreigh", "slaie", "slay", "sleigh", "smay", "snay", "spey", "splay", "spray", "stay", "stray", "strey", "sway", "tae", "tay", "they", "tray", "tre", "trey", "vey", "way", "waye", "wei", "weigh", "wey", "whey", "wray", "wy", "yay", "yea"}, RecognizedState.MUSIC),
            new VoiceCommand(new String[]{"uber","didi", "vault", "jackson", "DD", "get me", "beady", "beedie", "beedy", "bee de", "taxi", "greedy", "greed he", "guidi", "heedy", "heed he", "keady", "keedy,", "leady", "lea di", "midi", "mi de", "mi di", "needy", "need e", "need he", "reedy", "seedy", "tweedie", "tweedy", "weedy"}, RecognizedState.DIDI),
            new VoiceCommand(new String[]{"resume", "asian", "review"}, RecognizedState.RESUME_MUSIC),
            new VoiceCommand(new String[]{"pause", "stop", "hold", "music", "old", "ghost", "folk", "bose", "boost", "ahs", "baas", "baus", "brause", "broz", "cars", "cause", "chas", "chaws", "chazz", "claus", "clause", "claws", "coz", "craws", "daus", "dawes", "daws", "dnaase", "draus", "draws", "droz", "faas", "faus", "flaws", "fraas", "fs was", "gaus", "gause", "gauze", "gaz", "glaus", "gnaws", "hawes", "haws", "jaws", "kaus", "knaus", "laas", "lawes", "laws", "maahs", "maas", "mas", "maus", "maz", "mroz", "naas", "naus", "oz", "pas", "paws", "paz", "prause", "quass", "rosé", "roz", "saus", "sause", "saws", "schnoz", "schnozz", "shaws", "sias", "spas", "squaws", "staas", "straws", "tawes", "thaws", "vase", "waas", "was", "applause", "audas", "backsaws", "because", "bylaws", "cha-chas", "chainsaws", "coultas", "diaz", "dupras", "faux pas", "first cause", "francoise", "grandmas", "guffaws", "handsaws", "he was", "ijaz", "inlaws", "it was", "i was", "macaws", "mieras", "okaz", "outlaws", "poitras", "scofflaws", "that was", "tovaz", "warsaws", "what was", "withdraws"}, RecognizedState.PAUSE_MUSIC)
    };

    public StateCommandHelper(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public boolean parseCommand(String text) {
        for (VoiceCommand voiceCommand : voiceCommands) {
            for (String key : voiceCommand.keys) {
                if (text.toLowerCase().contains(key.toLowerCase().trim())) {
                    EventBus.getDefault().post(new VoiceCommandEvent(voiceCommand.resultState));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    private static class VoiceCommand {
        String[] keys;
        RecognizedState resultState;

        public VoiceCommand(String[] keys, RecognizedState resultState) {
            this.keys = keys;
            this.resultState = resultState;
        }
    }
}

package io.github.profjb58.lcr.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.abusereport.AbuseReportReasonScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.abusereport.AbuseReportReason;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(AbuseReportReasonScreen.class)
public class AbuseReportReasonScreenMixin extends Screen {

    @Shadow @Final
    private Screen parent;

    @Shadow @Final
    private Consumer<AbuseReportReason> reasonConsumer;

    @Shadow
    private AbuseReportReasonScreen.ReasonListWidget reasonList;

    protected AbuseReportReasonScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/screen/abusereport/AbuseReportReasonScreen;init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/abusereport/AbuseReportReasonScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"
            ),
            cancellable = true
    )
    private void init(CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget((this.width / 2) - 75, this.height - 20 - 4, 150, 20, ScreenTexts.DONE, (button) -> {
            AbuseReportReasonScreen.ReasonListWidget.ReasonEntry reasonEntry = this.reasonList.getSelectedOrNull();
            if (reasonEntry != null) {
                this.reasonConsumer.accept(reasonEntry.getReason());
            }
            MinecraftClient.getInstance().setScreen(this.parent);
        }));
        super.init();
        ci.cancel();
    }
}

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.SeparatorPlacement;
import com.intellij.openapi.util.Factory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageSearcher;
import com.intellij.util.Processor;
import com.intellij.xml.util.ColorIconCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.FindUsageUtils;
import utils.PropertiesUtils;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UsageCountLineProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (!ResourceUsageCountUtils.isTargetTagToCount(psiElement)) {
            return null;
        }
        //System.out.println("Start draw icon...");
        int count = findTagUsage((XmlTag) psiElement);
        Color color = count <= 0 ? PropertiesUtils.getZeroColor() : count == 1 ? PropertiesUtils.getOneColor() : PropertiesUtils.getOtherColor();
        if (PropertiesUtils.isOnlyShowZeroCount()) {
            //System.out.println("Only one ");
            return count == 0 ? new MyLineMarkerInfo(psiElement, count, color) : new MyLineMarkerInfo(psiElement, -1, color);
        }
        return new MyLineMarkerInfo(psiElement, count, color);
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection) {

    }

    private class MyLineMarkerInfo extends LineMarkerInfo<PsiElement> {

        public MyLineMarkerInfo(PsiElement element, int count, Color color) {
            //super(element, element.getTextRange(), new TextIcon(String.valueOf(count), null, color, 10), Pass.UPDATE_ALL, null, null, GutterIconRenderer.Alignment.LEFT);
            super(element, element.getTextRange(), new ColorIconCache.ColorIcon(8, color), Pass.UPDATE_ALL, null, null, GutterIconRenderer.Alignment.LEFT);
            //System.out.println("Draw finish..." + count + color);
            separatorPlacement = SeparatorPlacement.BOTTOM;
        }
    }

    private int findTagUsage(XmlTag element) {
        final FindUsagesHandler handler = FindUsageUtils.getFindUsagesHandler(element, element.getProject());
        if (handler != null) {
            final FindUsagesOptions findUsagesOptions = handler.getFindUsagesOptions();
            final PsiElement[] primaryElements = handler.getPrimaryElements();
            final PsiElement[] secondaryElements = handler.getSecondaryElements();
            Factory factory = new Factory() {
                public UsageSearcher create() {
                    return FindUsageUtils.createUsageSearcher(primaryElements, secondaryElements, handler, findUsagesOptions, (PsiFile) null);
                }
            };
            UsageSearcher usageSearcher = (UsageSearcher)factory.create();
            final AtomicInteger mCount = new AtomicInteger(0);
            usageSearcher.generate(new Processor<Usage>() {
                @Override
                public boolean process(Usage usage) {
                    if (ResourceUsageCountUtils.isUsefulUsageToCount(usage)) {
                        mCount.incrementAndGet();
                    }
                    return true;
                }
            });
            //System.out.println(usageSearcher.toString());
            //System.out.println("count: " + mCount.get());
            return mCount.get();
        }
        return 0;
    }

}
package step2.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import step2.domain.Lotto;
import step2.domain.LottoRank;
import step2.domain.LottoStatistics;
import step2.domain.LottoStore;
import step2.domain.Lottos;
import step2.domain.Money;
import step2.domain.WinningLotto;

public class OutputView {

    public static void printBuyLotto(final Lottos lottos, final Money money, final int countOfselectedLottos) {
        final Money costOfSelectedLottos = new Money(LottoStore.LOTTO_PRICE.getMoney() * countOfselectedLottos);
        final Money restMoney = money.subtractMoney(costOfSelectedLottos);

        System.out.printf("\n수동으로 %d장, 자동으로 %d개를 구매했습니다.\n", countOfselectedLottos, restMoney.getMoney() / LottoStore.LOTTO_PRICE.getMoney());
        for (Lotto lotto : lottos.getLottos()) {
            System.out.println(lotto);
        }
    }

    public static void printLottoStatistics(WinningLotto winningLotto, LottoStatistics lottoStatistics) {
        System.out.println("\n당첨 통계\n---------");

        Map<Integer, List<LottoRank>> groupByRanks = lottoStatistics.getMyRanks(winningLotto)
                                                                    .stream()
                                                                    .collect(Collectors.groupingBy(LottoRank::getRank));
        for (LottoRank rank : LottoRank.values()) {
            groupByRanks.putIfAbsent(rank.getRank(), List.of());
        }

        groupByRanks.entrySet()
                    .stream()
                    .filter(rank -> rank.getKey() < LottoRank.NO_RANK.getRank())
                    .forEach(entry -> System.out.println(LottoRank.rankOf(entry.getKey()).getEtc() + " - " + entry.getValue().size() + "개"));

        System.out.printf("총 수익률은 %.2f 입니다.", lottoStatistics.getBenefitPercent(winningLotto));
    }
}

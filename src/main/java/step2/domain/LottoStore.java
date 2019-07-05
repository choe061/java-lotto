package step2.domain;

import java.util.List;
import java.util.function.Function;

import step2.dto.LottosDTO;

public class LottoStore {
    public static final Money LOTTO_PRICE = new Money(1_000L);
    private static final LottoStore INSTANCE = new LottoStore();

    private LottoStore() {}

    public static LottoStore getInstance() {
        return INSTANCE;
    }

    public Lottos salesLottos(final Money money, final LottosDTO lottosDTO) {
        final var userPickLottos = salesLottos(money, lottosDTO, new UserPickLottosFactory());

        final var usedMoney = userPickLottos.getTotalPrice();
        final var restMoney = money.subtractMoney(usedMoney);

        final var autoPickLottos = salesLottos(restMoney, new AutoPickLottosFactory());

        return userPickLottos.addAll(autoPickLottos);
    }

    private Lottos salesLottos(final Money money, final Function<LottoQuantity, Lottos> factory) {
        if (!has1000Won(money)) {
            return factory.apply(new LottoQuantity(0L));
        }

        final LottoQuantity quantity = money.getLottoQuantity(LOTTO_PRICE);
        return factory.apply(quantity);
    }

    public Lottos salesLottos(final Money money, LottosDTO lottosDTO, final Function<LottosDTO, Lottos> factory) {
        if (!has1000Won(money)) {
            return factory.apply(new LottosDTO(List.of()));
        }

        final LottoQuantity quantity = money.getLottoQuantity(LOTTO_PRICE);
        quantity.validateLottoSize(lottosDTO.getLottos().size());
        return factory.apply(lottosDTO);
    }

    private boolean has1000Won(final Money money) {
        if (money.getMoney() < LOTTO_PRICE.getMoney()) {
            return false;
        }
        return true;
    }
}

package com.ceaver.assin.action.input

import androidx.fragment.app.Fragment

class ActionInputFragment() : Fragment() {

//    private lateinit var viewModel: ActionInputViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        val args = ActionInputFragmentArgs.fromBundle(requireArguments())
//        viewModel = viewModels<ActionInputViewModel> { ActionInputViewModel.Factory(args.actionEntity, args.title, args.actionType) }.value
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        observeSymbols()
//        observeTrade()
//        observeDataReady()
//        return inflater.inflate(R.layout.action_input_fragment, container, false)
//    }
//
//    override fun onStart() {
//        super.onStart()
////        prepareView(ActionInputFragmentArgs.fromBundle(requireArguments()).actionType)
//        bindActions()
//        observeStatus()
//    }
//
//    private fun prepareView(actionType: ActionType) {
//        actionInputFragmentBuySymbolSpinner.isEnabled = false // not possible in XML
//        actionInputFragmentSellSymbolSpinner.isEnabled = false // not possible in XML
//        when (actionType) {
//            ActionType.TRADE -> {
//                actionInputFragmentTradeTypeTextView.text = "Trade"
//                actionInputFragmentTradeTypeImageView.setImageResource(com.ceaver.assin.R.drawable.trade)
//            }
//            ActionType.DEPOSIT -> {
//                actionInputFragmentTradeTypeTextView.text = "Deposit"
//                actionInputFragmentTradeTypeImageView.setImageResource(com.ceaver.assin.R.drawable.deposit)
//
//                actionInputFragment.removeView(actionInputFragmentSellTradeLabel)
//                actionInputFragment.removeView(actionInputFragmentSellAmountTextView)
//                actionInputFragment.removeView(actionInputFragmentSellSymbolSpinner)
//                actionInputFragment.removeView(actionInputFragmentBuyTradeLabel)
//
//                val constraintSet = ConstraintSet()
//                constraintSet.clone(actionInputFragment)
//                constraintSet.connect(actionInputFragmentCommentLabel.id, ConstraintSet.TOP, actionInputFragmentBuyAmountTextView.id, ConstraintSet.BOTTOM, 20)
//                constraintSet.connect(actionInputFragmentBuyAmountTextView.id, ConstraintSet.TOP, actionInputFragmentTradeTypeImageView.id, ConstraintSet.BOTTOM, 20)
//                constraintSet.applyTo(actionInputFragment)
//            }
//            ActionType.WITHDRAW -> {
//                actionInputFragmentTradeTypeTextView.text = "Withdraw"
//                actionInputFragmentTradeTypeImageView.setImageResource(com.ceaver.assin.R.drawable.withdraw)
//
//                actionInputFragment.removeView(actionInputFragmentBuyTradeLabel)
//                actionInputFragment.removeView(actionInputFragmentBuyAmountTextView)
//                actionInputFragment.removeView(actionInputFragmentBuySymbolSpinner)
//                actionInputFragment.removeView(actionInputFragmentSellTradeLabel)
//
//                val constraintSet = ConstraintSet()
//                constraintSet.clone(actionInputFragment)
//                constraintSet.connect(actionInputFragmentSellAmountTextView.id, ConstraintSet.TOP, actionInputFragmentTradeTypeImageView.id, ConstraintSet.BOTTOM, 20)
//                constraintSet.applyTo(actionInputFragment)
//            }
//        }
//        actionInputFragmentTradeDateTextView.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                val actionDate = CalendarHelper.convertDate(actionInputFragmentTradeDateTextView.text.toString())
//                val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth -> actionInputFragmentTradeDateTextView.setText(CalendarHelper.convertDate(LocalDate.of(year, monthOfYear + 1, dayOfMonth))); actionInputFragmentTradeDateTextView.clearFocus() }
//                val datePickerDialog = DatePickerDialog(this@ActionInputFragment.requireContext(), dateSetListener, actionDate.year, actionDate.monthValue - 1, actionDate.dayOfMonth)
//                datePickerDialog.show()
//            }
//        }
//        actionInputFragmentTradeDateTextView.keyListener = null // hack to disable user input
//    }
//
//    private fun bindActions() {
//        actionInputFragmentSaveButton.setOnClickListener { onSaveClick() }
//    }
//
//    private fun onSaveClick() {
//        val comment = actionInputFragmentCommentTextView.text.toString().ifEmpty { null }
//        val actionDate = CalendarHelper.convertDate(actionInputFragmentTradeDateTextView.text.toString())
////        when (ActionInputFragmentArgs.fromBundle(requireArguments()).actionType) {
////            ActionType.TRADE -> {
////                val buyTitle = actionInputFragmentBuySymbolSpinner.selectedItem as Title
////                val buyAmount = actionInputFragmentBuyAmountTextView.text.toString().toBigDecimal()
////                val sellTitle = actionInputFragmentSellSymbolSpinner.selectedItem as Title
////                val sellAmount = actionInputFragmentSellAmountTextView.text.toString().toBigDecimal()
////                val valueBtc = sellTitle.cryptoQuotes.price.toBigDecimal(MathContext.DECIMAL32).times(sellAmount)
////                val valueUsd = sellTitle.fiatQuotes.price.toBigDecimal(MathContext.DECIMAL32).times(sellAmount)
////                viewModel.onSaveTradeClick(buyTitle, buyAmount, sellTitle, sellAmount, actionDate, comment, valueBtc, valueUsd)
////            }
////            ActionType.DEPOSIT -> {
////                val buyTitle = actionInputFragmentBuySymbolSpinner.selectedItem as Title
////                val buyAmount = actionInputFragmentBuyAmountTextView.text.toString().toBigDecimal()
////                val valueBtc = buyTitle.cryptoQuotes.price.toBigDecimal(MathContext.DECIMAL32).times(buyAmount)
////                val valueUsd = buyTitle.fiatQuotes.price.toBigDecimal(MathContext.DECIMAL32).times(buyAmount)
////                viewModel.onDepositClick(buyTitle, buyAmount, actionDate, comment, valueBtc, valueUsd)
////            }
////            ActionType.WITHDRAW -> {
////                val sellTitle = actionInputFragmentSellSymbolSpinner.selectedItem as Title
////                val sellAmount = actionInputFragmentSellAmountTextView.text.toString().toBigDecimal()
////                val valueBtc = sellTitle.cryptoQuotes.price.toBigDecimal(MathContext.DECIMAL32).times(sellAmount)
////                val valueUsd = sellTitle.fiatQuotes.price.toBigDecimal(MathContext.DECIMAL32).times(sellAmount)
////                viewModel.onWithdrawClick(sellTitle, sellAmount, actionDate, comment, valueBtc, valueUsd)
////            }
//            else -> throw IllegalStateException()
//        }
//    }
//
//    private fun observeSymbols() {
//        viewModel.symbols.observe(viewLifecycleOwner, Observer {
//            val adapter = ArrayAdapter<Title>(this.requireContext(), android.R.layout.simple_spinner_item)
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            actionInputFragmentBuySymbolSpinner.adapter = adapter
//            actionInputFragmentSellSymbolSpinner.adapter = adapter
//            adapter.addAll(it!!) })
//    }
//
//    private fun observeTrade() {
//        viewModel.actionEntity.observe(viewLifecycleOwner, Observer {
//            publishFields(it!!);
//        })
//    }
//
//    private fun observeDataReady() {
//        viewModel.dataReady.observe(viewLifecycleOwner, Observer {
//            updateSpinnerFields(it!!.first)
//            registerInputValidation()
//            enableInput(true)
//            viewModel.dataReady.removeObservers(this)
//        })
//    }
//
//    private fun updateSpinnerFields(actionEntity: ActionEntity) {
////        if (actionEntity.buyTitleId != null) {
////            actionInputFragmentBuySymbolSpinner.setSelection(viewModel.symbols.value!!.indexOf(actionEntity.buyTitle!!))
////        }
////        if (actionEntity.sellTitleId != null) {
////            actionInputFragmentSellSymbolSpinner.setSelection(viewModel.symbols.value!!.indexOf(actionEntity.sellTitle!!))
////        }
//    }
//
//    private fun publishFields(actionEntity: ActionEntity) {
////        actionInputFragmentTradeDateTextView.setText(CalendarHelper.convertDate(actionEntity.actionDate))
////        actionInputFragmentCommentTextView.setText(actionEntity.comment.orEmpty())
////        when (ActionInputFragmentArgs.fromBundle(requireArguments()).actionType) {
////            ActionType.TRADE -> {
////                actionInputFragmentBuyAmountTextView.setText(if (actionEntity.buyQuantity != null) actionEntity.buyQuantity.toString() else "")
////                actionInputFragmentSellAmountTextView.setText(if (actionEntity.sellQuantity != null) actionEntity.sellQuantity.toString() else "")
////            }
////            ActionType.DEPOSIT -> {
////                actionInputFragmentBuyAmountTextView.setText(if (actionEntity.buyQuantity != null) actionEntity.buyQuantity.toString() else "")
////            }
////            ActionType.WITHDRAW -> {
////                actionInputFragmentSellAmountTextView.setText(if (actionEntity.sellQuantity != null) actionEntity.sellQuantity.toString() else "")
////            }
////            else -> throw IllegalStateException()
////        }
//    }
//
//    private fun observeStatus() {
//        viewModel.status.observe(this, Observer {
//            when (it) {
//                ActionInputViewModel.ActionInputStatus.START_SAVE -> onStartSave()
//                ActionInputViewModel.ActionInputStatus.END_SAVE -> onEndSave()
//                null -> throw IllegalStateException()
//            }
//        })
//    }
//
//    private fun onStartSave() {
//        enableInput(false)
//    }
//
//    private fun enableInput(enable: Boolean) {
//        actionInputFragmentSaveButton.isEnabled = enable && checkSaveButton()
//        actionInputFragmentCommentTextView.isEnabled = enable
//        actionInputFragmentTradeDateTextView.isEnabled = enable
//        when (ActionInputFragmentArgs.fromBundle(requireArguments()).actionType) {
//            ActionType.TRADE -> {
//                actionInputFragmentBuyAmountTextView.isEnabled = enable
//                actionInputFragmentBuySymbolSpinner.isEnabled = enable
//                actionInputFragmentSellAmountTextView.isEnabled = enable
//                actionInputFragmentSellSymbolSpinner.isEnabled = enable
//            }
//            ActionType.DEPOSIT -> {
//                actionInputFragmentBuyAmountTextView.isEnabled = enable
//                actionInputFragmentBuySymbolSpinner.isEnabled = enable
//            }
//            ActionType.WITHDRAW -> {
//                actionInputFragmentSellAmountTextView.isEnabled = enable
//            }
//            else -> throw IllegalStateException()
//        }
//    }
//
//    private fun onEndSave() {
//        findNavController().navigateUp()
//    }
//
//    private fun registerInputValidation() {
//        when (ActionInputFragmentArgs.fromBundle(requireArguments()).actionType) {
//            ActionType.TRADE -> {
////                actionInputFragmentBuyAmountTextView.registerInputValidator({ s -> s.isNotEmpty() }, "Please enter amount")
////                actionInputFragmentSellAmountTextView.registerInputValidator({ s -> s.isNotEmpty() }, "Please enter amount")
////                actionInputFragmentBuyAmountTextView.afterTextChanged { actionInputFragmentSaveButton.isEnabled = checkSaveButton() }
////                actionInputFragmentSellAmountTextView.afterTextChanged { actionInputFragmentSaveButton.isEnabled = checkSaveButton() }
//            }
//            ActionType.DEPOSIT -> {
////                actionInputFragmentBuyAmountTextView.registerInputValidator({ s -> (s.isNotEmpty()) }, "Please enter amount")
////                actionInputFragmentBuyAmountTextView.afterTextChanged { actionInputFragmentSaveButton.isEnabled = checkSaveButton() }
//            }
//            ActionType.WITHDRAW -> {
////                actionInputFragmentSellAmountTextView.registerInputValidator({ s -> (s.isNotEmpty()) }, "Please enter amount")
////                actionInputFragmentSellAmountTextView.afterTextChanged { actionInputFragmentSaveButton.isEnabled = checkSaveButton() }
//            }
//            else -> throw IllegalStateException()
//        }
//    }
//
//    private fun checkSaveButton(): Boolean {
//        return actionInputFragmentBuyAmountTextView.error == null && actionInputFragmentSellAmountTextView.error == null
//    }
}

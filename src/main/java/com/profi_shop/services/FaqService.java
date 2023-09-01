package com.profi_shop.services;

import com.profi_shop.exceptions.SearchException;
import com.profi_shop.model.Faq;
import com.profi_shop.repositories.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqService {
    private final FaqRepository faqRepository;

    @Autowired
    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public void createFaq(String question, String answer){
        Faq faq = new Faq();
        faq.setAnswer(answer);
        faq.setQuestion(question);
        faqRepository.save(faq);
    }

    public List<Faq> getAllFaqs(){
        return faqRepository.findAll();
    }

    public void deleteFaqById(Long faqId) {
        Faq faq = faqRepository.findById(faqId).orElseThrow(() -> new SearchException(SearchException.FAQ_NOT_FOUND));
        faqRepository.delete(faq);
    }
}

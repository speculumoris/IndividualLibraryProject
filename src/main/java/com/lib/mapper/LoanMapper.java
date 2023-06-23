package com.lib.mapper;

import com.lib.domain.ImageFile;
import com.lib.domain.Loan;
import com.lib.domain.User;
import com.lib.dto.LoanDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LoanMapper {


    LoanDTO loanToLoanDTO(Loan loan);
    List<LoanDTO> map(List<Loan> loanList);

    @Named("getImageAsString")
    public static Set<String> getImageIds(Set<ImageFile> imageFiles) {
        Set<String> imgs = new HashSet<>();

        imgs = imageFiles.stream().map(imFile->imFile.getId().toString()).
                collect(Collectors.toSet());
        return imgs;
    }
    @Named("getUserId")
    public static Long getUserId(User user) {
        return user.getId();
    }

}

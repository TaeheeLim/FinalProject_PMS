package com.kanboo.www.service.impl.project;

import com.kanboo.www.domain.entity.member.Member;
import com.kanboo.www.domain.entity.project.Calendar;
import com.kanboo.www.domain.entity.project.Project;
import com.kanboo.www.domain.repository.member.MemberRepository;
import com.kanboo.www.domain.repository.project.CalendarRepository;
import com.kanboo.www.domain.repository.project.ProjectRepository;
import com.kanboo.www.dto.member.MemberDTO;
import com.kanboo.www.dto.project.CalendarDTO;
import com.kanboo.www.service.inter.project.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

	private final CalendarRepository calendarRepository;
	private final ProjectRepository projectRepository;
	private final MemberRepository memberRepository;

	@Override
	public List<CalendarDTO> calendarHandler(CalendarDTO calendarDTO) {
		Map<String, Object> map = new HashMap<>();
		map.put("member", calendarDTO.getMember().getMemIdx());
		List<Calendar> calendarList = calendarRepository
				.findByProjectIdxAndMemberIdx(calendarDTO.getProject().getPrjctIdx()
						, map);
		List<CalendarDTO> dtoList = new ArrayList<>();
		for (Calendar calendar : calendarList) {
			dtoList.add( calendar.entityToDto() );
		}
		return dtoList;
	}

	@Override
	@Transactional
	public CalendarDTO updateCalendar(CalendarDTO calendarDTO) {
		// dto에 있는 idx로 entity조회
		// 반환된 entity에 setter메소드(생성자x)를 통해서 값 업데이트
			Calendar calendar = calendarRepository.findByCalIdx(calendarDTO.getCalIdx());
			calendar.changeStartDate(calendarDTO.getCalStartDate());
			calendar.changeEndDate(calendarDTO.getCalEndDate());
			if(calendar.getCalTitle().equals(calendarDTO.getCalTitle())){
				calendar.changeTitle(calendarDTO.getCalTitle());
			}
		return calendar.entityToDto();
	}

	@Override
	public CalendarDTO insertCalendar(CalendarDTO calendarDTO) {
		Project project = projectRepository.findByPrjctIdx(calendarDTO.getProject().getPrjctIdx());
		Member member = memberRepository.findByMemIdx(calendarDTO.getProject().getPrjctIdx());
		Member findRoleByMemIdx = memberRepository.findByMemIdx(calendarDTO.getMember().getMemIdx());

		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setRole( findRoleByMemIdx.getRole().entityToDto() );
		calendarDTO.setProject(project.entityToDto());
		calendarDTO.setMember(member.entityToDto());
		calendarDTO.getMember().setRole(memberDTO.getRole());

		Calendar calendar = calendarRepository.save(calendarDTO.dtoToEntity());
		return calendar.entityToDto();
	}
}
